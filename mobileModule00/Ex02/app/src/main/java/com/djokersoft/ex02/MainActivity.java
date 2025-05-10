package com.djokersoft.ex02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.res.Configuration;
public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private TextView text0, text1;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonDot, buttonDots, buttonPlus, buttonMinus, buttonMul, buttonDiv, buttonAC, buttonC, buttonReturn;

    private String currentInput = "";
    private double num1 = 0;
    private String pendingOperation = "";
    private boolean operationClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar TextViews
        text0 = findViewById(R.id.text0);
        text1 = findViewById(R.id.text1);

        // Inicializar botões numéricos
        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);

        // Inicializar outros botões
        buttonDot = findViewById(R.id.button_dot);
        buttonDots = findViewById(R.id.button_dots);
        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_minus);
        buttonMul = findViewById(R.id.button_mul);
        buttonDiv = findViewById(R.id.button_div);
        buttonAC = findViewById(R.id.button_ac);
        buttonC = findViewById(R.id.button_c);
        buttonReturn = findViewById(R.id.button_return);

        // Configurar listeners para todos os botões
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonDots.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonMul.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);
        buttonAC.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);

        // Inicializar os displays
        text0.setText("0");
        text1.setText("0");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Processamento de botões numéricos
        if (id == R.id.button_0) {
            processNumericInput("0");
        } else if (id == R.id.button_1) {
            processNumericInput("1");
        } else if (id == R.id.button_2) {
            processNumericInput("2");
        } else if (id == R.id.button_3) {
            processNumericInput("3");
        } else if (id == R.id.button_4) {
            processNumericInput("4");
        } else if (id == R.id.button_5) {
            processNumericInput("5");
        } else if (id == R.id.button_6) {
            processNumericInput("6");
        } else if (id == R.id.button_7) {
            processNumericInput("7");
        } else if (id == R.id.button_8) {
            processNumericInput("8");
        } else if (id == R.id.button_9) {
            processNumericInput("9");
        } else if (id == R.id.button_dot) {
            if (!currentInput.contains(".")) {
                if (currentInput.isEmpty()) {
                    currentInput = "0.";
                } else {
                    currentInput += ".";
                }
                text1.setText(currentInput);
            }
            Log.d("MainActivity", ".");
        } else if (id == R.id.button_dots) {
            processNumericInput("00");

        }
        // Processamento de operações
        else if (id == R.id.button_plus) {
            processOperation("+");
        } else if (id == R.id.button_minus) {
            processOperation("-");
        } else if (id == R.id.button_mul) {
            processOperation("*");
        } else if (id == R.id.button_div) {
            processOperation("/");
        } else if (id == R.id.button_return) {
            Log.d("MainActivity", "Return");
        }
        // Limpar entrada
        else if (id == R.id.button_c) {
            currentInput = "";
            text1.setText("0");
            Log.d("MainActivity", "Clear");
        }
        // Limpar tudo
        else if (id == R.id.button_ac) {
            currentInput = "";
            num1 = 0;
            pendingOperation = "";
            operationClicked = false;
            text0.setText("0");
            text1.setText("0");
            Log.d("MainActivity", "Clear All");
        }
    }

    private void processNumericInput(String digit)
    {
        Log.d("MainActivity", "Digit:"+ digit);
    }

    private void processOperation(String operation)
    {

        Log.d("MainActivity", "Operation:"+ operation);

    }

    private void checkOrientation()
    {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            // O Android já vai carregar o layout alternativo (layout-land)
            // mas podemos fazer ajustes adicionais por código se necessário
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        checkOrientation();

        // Opcionalmente, podemos salvar e restaurar o estado da calculadora aqui
        // Para garantir  não perca o cálculo atual ao girar a tela
    }

    private void calculateResult()
    {

    }


    private String formatNumber(double number)
    {
        if (number == (long) number)
        {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number);

        }
    }
}