package com.djokersoft.swiftycompanion.service;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.djokersoft.swiftycompanion.data.Config;
import com.djokersoft.swiftycompanion.data.CursusUser;
import com.djokersoft.swiftycompanion.data.Project;
import com.djokersoft.swiftycompanion.data.ProjectUser;
import com.djokersoft.swiftycompanion.data.TokenManager;
import com.djokersoft.swiftycompanion.data.User;
import com.djokersoft.swiftycompanion.data.UserBasic;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson
        ;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private static final String TAG = "djokersoft";

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }


    public static void generateClientCredentialsToken(Context context, ApiCallback<Boolean> callback)
    {

     //   Log.d(TAG, "Using Client ID: " + Config.CLIENT_ID);
    //    Log.d(TAG, "Using Client Secret: " + Config.CLIENT_SECRET.substring(0, 5) + "...");
        final boolean isAuthenticating = false;
        new Thread(() -> {
            try {



                FormBody formBody = new FormBody.Builder()
                        .add("grant_type", "client_credentials")
                        .add("client_id", Config.CLIENT_ID)
                        .add("client_secret", Config.CLIENT_SECRET)
                        .build();

                Request request = new Request.Builder()
                        .url(Config.TOKEN_URL)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful())
                {
                    Log.d(TAG,"Error generating token:"+response);

                    throw new IOException("Unexpected code " + response);
                }

                String responseBody = response.body().string();

                JSONObject jsonObject = new JSONObject(responseBody);
                String accessToken = jsonObject.getString("access_token");
                long expiresIn = jsonObject.getLong("expires_in"); // Normalmente 7200 segundos (2 horas)


                TokenManager.getInstance(context).storeToken(accessToken, System.currentTimeMillis() + (expiresIn * 1000));


                callback.onSuccess(true);

            } catch (Exception e)
            {

                Log.e(TAG, "Error generating token", e);
                callback.onError("Error generating token: " + e.getMessage());
            }
        }).start();

    }


    public static void ensureValidToken(Context context, ApiCallback<Boolean> callback) {


        TokenManager tokenManager = TokenManager.getInstance(context);

        if (!tokenManager.hasToken() || tokenManager.isTokenExpired())
        {

            Log.d(TAG, "Getting new token");
            generateClientCredentialsToken(context, callback);
        } else
        {
            Log.d(TAG, "Token is valid");
            callback.onSuccess(true);
        }
    }


    public static void searchUser(Context context, String username, ApiCallback<User> callback) {
        if (username == null || username.isEmpty()) {
            callback.onError("Username cannot be empty");
            return;
        }


        ensureValidToken(context, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isValid) {
                if (isValid)
                {
                    fetchUserByLogin(context, username, callback);
                } else {
                    callback.onError("Failed to authenticate with API");
                }
            }

            @Override
            public void onError(String errorMessage)
            {
                callback.onError(errorMessage);
            }
        });
    }


    private static void fetchUserByLogin(Context context, String username, ApiCallback<User> callback) {
        TokenManager tokenManager = TokenManager.getInstance(context);
        String accessToken = tokenManager.getAccessToken();


        Request request = new Request.Builder()
                .url(Config.API_BASE_URL + "users?filter[login]=" + username.toLowerCase())
                .header("Authorization", "Bearer " + accessToken)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();

                if (response.code() == 401)
                {
                    // Token inválido, gerar novo e tentar novamente
                    generateClientCredentialsToken(context, new ApiCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            fetchUserByLogin(context, username, callback);
                        }

                        @Override
                        public void onError(String errorMessage)
                        {
                            callback.onError(errorMessage);
                        }
                    });
                    return;
                }

                if (!response.isSuccessful())
                {
                    callback.onError("API Error: " + response.code() + " " + response.message());
                    return;
                }

                String responseBody = response.body().string();


                Type userListType = new TypeToken<ArrayList<UserBasic>>(){}.getType();
                List<UserBasic> users = gson.fromJson(responseBody, userListType);

                if (users.isEmpty()) {

                    callback.onError("User not found");
                    Log.d(TAG,"User not found");
                    return;
                }


                fetchUserDetails(context, users.get(0).getId(), callback);

            } catch (Exception e) {
                Log.e(TAG, "Error fetching user details", e);
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }


    private static void fetchUserProjects(Context context, User user, ApiCallback<User> callback) {
        TokenManager tokenManager = TokenManager.getInstance(context);
        String accessToken = tokenManager.getAccessToken();

        if (accessToken == null) {
            callback.onSuccess(user); // Retorna o usuário sem projetos
            return;
        }

        Request request = new Request.Builder()
                .url(Config.API_BASE_URL + "users/" + user.getId() + "/projects_users")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {

                    Log.e(TAG, "Error fetching projects: " + response.code());
                    deliverSuccess(context, callback, user);
                    return;
                }

     

                String responseBody = response.body().string();
                Log.d(TAG, "Projects response received:+"+accessToken);
                //saveResponseToFile(context, "user_projects.json", responseBody);


                Type projectUserListType = new TypeToken<List<ProjectUser>>(){}.getType();
                List<ProjectUser> projectsUsers = gson.fromJson(responseBody, projectUserListType);

                if (projectsUsers != null && !projectsUsers.isEmpty()) {

                    List<Project> projects = new ArrayList<>();
                    int completedProjects = 0;
                    int failedProjects = 0;

                    for (ProjectUser projectUser : projectsUsers) {
                        if (projectUser.getProject() != null) {
                            Project project = projectUser.getProject();
                            project.setStatus(projectUser.getStatus());

                            // Definir a nota, pode ser null
                            if (projectUser.getFinalMark() != null) {
                                project.setFinalMark(projectUser.getFinalMark());
                            }

                            // Definir se foi validado
                            project.setValidated(projectUser.getValidated() != null && projectUser.getValidated() ? "true" : "false");

                            // Definir texto de status para exibição
                            if ("finished".equals(projectUser.getStatus())) {
                                if (projectUser.getValidated() != null && projectUser.getValidated()) {
                                    project.setStatusDisplay("Validated");
                                    completedProjects++;
                                } else if (projectUser.getFinalMark() != null && projectUser.getFinalMark() >= 50) {
                                    project.setStatusDisplay("Passed");
                                    completedProjects++;
                                } else {
                                    project.setStatusDisplay("Failed");
                                    failedProjects++;
                                }
                            } else if ("in_progress".equals(projectUser.getStatus())) {
                                project.setStatusDisplay("In Progress");
                            } else {
                                project.setStatusDisplay(projectUser.getStatus());
                            }

                            projects.add(project);
                        }
                    }

                    // Ordenar projetos (terminados primeiro, depois por nota)
                    Collections.sort(projects, (p1, p2) -> {
                        // Primeiro por status
                        if (!p1.getStatus().equals(p2.getStatus())) {
                            if ("finished".equals(p1.getStatus())) return -1;
                            if ("finished".equals(p2.getStatus())) return 1;
                        }

                        return Double.compare(p2.getFinalMark(), p1.getFinalMark());
                    });

                    // Atualizar o usuário
                    user.setProjects(projects);
                    user.setCompletedProjects(completedProjects);
                    user.setFailedProjects(failedProjects);
                }


                deliverSuccess(context, callback, user);

            } catch (Exception e) {
                Log.e(TAG, "Error processing projects", e);
                deliverSuccess(context, callback, user);
            }
        }).start();
    }



    private static void fetchUserDetails(Context context, int userId, ApiCallback<User> callback) {
        TokenManager tokenManager = TokenManager.getInstance(context);
        String accessToken = tokenManager.getAccessToken();

        Request request = new Request.Builder()
                .url(Config.API_BASE_URL + "users/" + userId)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();



                if (response.code() == 401) {
                    // Token inválido, gerar novo e tentar novamente
                    generateClientCredentialsToken(context, new ApiCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            fetchUserDetails(context, userId, callback);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            deliverError(context, callback, errorMessage);
                        }
                    });
                    return;
                }

                if (!response.isSuccessful()) {
                    deliverError(context, callback, "API Error: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                Log.d(TAG, "User details response received");
                saveResponseToFile(context, "user_details.json", responseBody);


                User user = gson.fromJson(responseBody, User.class);


                processUserData(user);


                fetchUserProjects(context, user, new ApiCallback<User>() {
                    @Override
                    public void onSuccess(User userWithProjects) {
                        deliverSuccess(context, callback, userWithProjects);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        deliverError(context, callback, errorMessage);
                    }
                });

            } catch (Exception e) {
               Log.e(TAG, "Error fetching user details", e);
                deliverError(context, callback, "Error fetching user details: " + e.getMessage());
            }
        }).start();
    }


    private static void processUserData(User user) {
        try {
            // Verificar e processar dados do cursus
            if (user.getCursusUsers() != null && !user.getCursusUsers().isEmpty()) {

                CursusUser mainCursus = null;
                for (CursusUser cursus : user.getCursusUsers()) {
                    if (cursus.getCursusId() == 21) { // 21 é o ID do cursus 42
                        mainCursus = cursus;
                        break;
                    }
                }

                // Se não encontrar o cursus 42, usar o último
                if (mainCursus == null && !user.getCursusUsers().isEmpty()) {
                    mainCursus = user.getCursusUsers().get(user.getCursusUsers().size() - 1);
                }

                if (mainCursus != null) {
                    user.setLevel(mainCursus.getLevel());
                    if (mainCursus.getSkills() != null) {
                        user.setSkills(mainCursus.getSkills());
                    }
                }
            }

            // Garantir que location tenha um valor
            if (user.getLocation() == null || user.getLocation().isEmpty()) {
                user.setLocation("Not available");
            }

            // Garantir que skills não seja null
            if (user.getSkills() == null) {
                user.setSkills(new ArrayList<>());
            }

            // Ordenar skills por nível
            Collections.sort(user.getSkills(), (s1, s2) -> Double.compare(s2.getLevel(), s1.getLevel()));

            // Definir valores padrão para projetos
            if (user.getProjects() == null) {
                user.setProjects(new ArrayList<>());
            }

            // Definir valores padrão para estatísticas
            user.setCompletedProjects(0);
            user.setFailedProjects(0);

            Log.d(TAG, "Processed user data: " + user.getLogin() + " | Level: " + user.getLevel() + " | Skills: " + user.getSkills().size());
        } catch (Exception e) {
            Log.e(TAG, "Error processing user data", e);
        }
    }

    private static <T> void deliverSuccess(Context context, ApiCallback<T> callback, T result) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> callback.onSuccess(result));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(result));
        }
    }
    private static <T> void deliverError(Context context, ApiCallback<T> callback, String error) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> callback.onError(error));
        } else {
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(error));
        }
    }
    public static void saveResponseToFile(Context context, String fileName, String responseData) {
        logLargeString(TAG, responseData);
        /*
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(responseData.getBytes());
            fos.close();
            Log.d(TAG, "Response saved to file: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error saving response to file", e);
        }*/
    }

    private static void logLargeString(String tag, String content) {
        if (content.length() > 4000) {
            int chunkCount = content.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = Math.min((i + 1) * 4000, content.length());
                Log.d(tag, "JSON PART " + i + ": " + content.substring(i * 4000, max));
            }
        } else {
            Log.d(tag, content);
        }
    }
}