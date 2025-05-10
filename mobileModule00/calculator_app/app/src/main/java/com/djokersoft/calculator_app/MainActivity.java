package com.djokersoft.calculator_app;




import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text0, text1;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonDot, buttonDots, buttonPlus, buttonMinus, buttonMul, buttonDiv, buttonAC, buttonC, buttonReturn;

    private StringBuilder inputExpression = new StringBuilder();
    private String currentInput = "";
    private boolean operationClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkOrientation();


        text0 = findViewById(R.id.text0);
        text1 = findViewById(R.id.text1);


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

        buttonDot = findViewById(R.id.button_dot);
        buttonDots = findViewById(R.id.button_dots);
        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_minus);
        buttonMul = findViewById(R.id.button_mul);
        buttonDiv = findViewById(R.id.button_div);
        buttonAC = findViewById(R.id.button_ac);
        buttonC = findViewById(R.id.button_c);
        buttonReturn = findViewById(R.id.button_return);


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
                inputExpression.append(currentInput.length() > 1 ? "." : currentInput);
            }
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
            calculateResult();
        }
        // Limpar entrada
        else if (id == R.id.button_c) {
            currentInput = "";
            text1.setText("0");
            // Se a expressão não estiver vazia, remova o último operador ou número
            if (inputExpression.length() > 0) {
                String expr = inputExpression.toString();
                // Se o último caractere é um operador (+, -, *, /)
                if (expr.endsWith("+") || expr.endsWith("-") || expr.endsWith("*") || expr.endsWith("/")) {
                    inputExpression.deleteCharAt(inputExpression.length() - 1);
                } else {
                    // Remova todos os dígitos do último número
                    int i = expr.length() - 1;
                    while (i >= 0 && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                        inputExpression.deleteCharAt(i);
                        i--;
                    }
                }
                text0.setText(inputExpression.toString());
            }
        }
        // Limpar tudo
        else if (id == R.id.button_ac) {
            currentInput = "";
            inputExpression.setLength(0);
            operationClicked = false;
            text0.setText("0");
            text1.setText("0");
        }
    }

    private void processNumericInput(String digit) {
        if (operationClicked) {
            currentInput = digit;
            operationClicked = false;
        } else {
            currentInput += digit;
        }
        text1.setText(currentInput);
        inputExpression.append(digit);
        text0.setText(inputExpression.toString());
    }

    private void processOperation(String operation) {
        if (!currentInput.isEmpty()) {
            inputExpression.append(operation);
            text0.setText(inputExpression.toString());
            operationClicked = true;
            currentInput = "";
        } else if (inputExpression.length() > 0) {
            // Se a expressão não está vazia e o último caractere já é um operador, substitua-o
            String expr = inputExpression.toString();
            char lastChar = expr.charAt(expr.length() - 1);
            if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                inputExpression.deleteCharAt(inputExpression.length() - 1);
                inputExpression.append(operation);
                text0.setText(inputExpression.toString());
            }
        }
    }

    private void calculateResult() {
        if (inputExpression.length() > 0) {
            String expression = inputExpression.toString();

            try {
                // Avalia a expressão
                Expression e = new ExpressionBuilder(expression).build();
                double result = e.evaluate();

                // Formata e exibe o resultado
                String resultStr = formatNumber(result);
                text1.setText(resultStr);

                // Atualiza o estado da calculadora
                inputExpression.setLength(0);
                inputExpression.append(resultStr);
                text0.setText(expression + " = " + resultStr);
                currentInput = resultStr;
                operationClicked = true;

            } catch (ArithmeticException ex) {
                // Lida com erros matemáticos (ex: divisão por zero)
                text1.setText("Erro");
                Toast.makeText(this, "Erro matemático: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                // Lida com outros erros de expressão
                text1.setText("Erro");
                Toast.makeText(this, "Expressão inválida", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentInput", currentInput);
        outState.putString("inputExpression", inputExpression.toString());
        outState.putBoolean("operationClicked", operationClicked);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentInput = savedInstanceState.getString("currentInput", "");
        String expr = savedInstanceState.getString("inputExpression", "");
        inputExpression = new StringBuilder(expr);
        operationClicked = savedInstanceState.getBoolean("operationClicked", false);

        // Restaura o estado visual da calculadora
        text1.setText(currentInput.isEmpty() ? "0" : currentInput);
        text0.setText(expr.isEmpty() ? "0" : expr);
    }

    // Verifica a orientação da tela e ajusta elementos conforme necessário
    private void checkOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // O Android vai carregar o layout alternativo (layout-land)

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Atualizar a interface quando a orientação mudar
        checkOrientation();


    }
}