package com.example.mycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv;
    MaterialButton buttonAC,buttonBrack, buttonPercentage;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEqual;
    MaterialButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    MaterialButton buttonDot, buttonC;
    Boolean ended, parentesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_main);
        ended = false;
        parentesis = false;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultTv = findViewById(R.id.result_tv);

        assignId(buttonAC, R.id.buttonAC);
        assignId(buttonC, R.id.buttonC);
        assignId(buttonBrack, R.id.buttonBrack);
        assignId(buttonPercentage, R.id.buttonPercentage);
        assignId(buttonDivide, R.id.buttonDivide);
        assignId(buttonMultiply, R.id.buttonMultiply);
        assignId(buttonPlus, R.id.buttonPlus);
        assignId(buttonMinus, R.id.buttonMinus);
        assignId(buttonEqual, R.id.buttonEqual);
        assignId(button1, R.id.button1);
        assignId(button2, R.id.button2);
        assignId(button3, R.id.button3);
        assignId(button4, R.id.button4);
        assignId(button5, R.id.button5);
        assignId(button6, R.id.button6);
        assignId(button7, R.id.button7);
        assignId(button8, R.id.button8);
        assignId(button9, R.id.button9);
        assignId(button0, R.id.button0);
        assignId(buttonDot, R.id.buttonDot);
    }

    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = resultTv.getText().toString();

        switch (buttonText) {
            case "AC":
                resultTv.setText("0");
                return;
            case "=":
                resultTv.setText(getResult(dataToCalculate));
                ended = true;
                return;
            case "C":
                if (dataToCalculate.length() > 1){
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                } else {
                    dataToCalculate = "0";
                }
                break;
            case "+":
                dataToCalculate += "+";
                ended = false;
                break;
            case "−":
                dataToCalculate += "-";
                ended = false;
                break;
            case "×":
                dataToCalculate += "*";
                ended = false;
                break;
            case "÷":
                dataToCalculate += "/";
                ended = false;
                break;
            case "( )":
                if (!parentesis) {
                    dataToCalculate += "(";
                } else {
                    dataToCalculate += ")";
                }
                ended = false;
                break;
            default:
                if (dataToCalculate.equals("0") | ended) {
                    dataToCalculate = "";
                    ended = false;
                }
                dataToCalculate += buttonText;
                break;
        }

        resultTv.setText(dataToCalculate);
    }

    public String getResult(String data) {
        double result = 0;
        double currentNumber = 0;
        char operator = '+';
        double labelulle = 0; // For managing percentages
        boolean decimalFlag = false; // For decimals
        int decimalPlace = 1; // To handle decimal places
        boolean isNegative = false; // For negative numbers

        for (int i = 0; i < data.length(); i++) {
            char current = data.charAt(i);

            // Handling digits
            if (Character.isDigit(current)) {
                if (decimalFlag) {
                    currentNumber += (current - '0') / (double) (decimalPlace *= 10);
                } else {
                    currentNumber = currentNumber * 10 + (current - '0');
                }
            }

            // Handle decimal point
            if (current == '.') {
                decimalFlag = true;
                continue;
            }

            // Handle negative sign
            if (current == '-') {
                isNegative = true;
            }

            if (!Character.isDigit(current) && current != ' ' || i == data.length() - 1) {
                if (isNegative) {
                    currentNumber = -currentNumber; // Apply negative sign
                    isNegative = false; // Reset negative flag
                }

                // Perform calculations based on the operator
                switch (operator) {
                    case '*':
                        result *= currentNumber;
                        break;
                    case '/':
                        if (currentNumber != 0) {
                            result /= currentNumber;
                        } else {
                            return "Can't divide by zero";
                        }
                        break;
                    case '+':
                        result += currentNumber;
                        break;
                    case '-':
                        result -= currentNumber;
                        break;
                    case '%':
                        if (currentNumber > 0) {
                            result += result * (currentNumber / 100);
                        }
                        break;
                }

                operator = current; // Update operator for next operation
                currentNumber = 0; // Reset current number
                decimalFlag = false; // Reset decimal flag
                decimalPlace = 1; // Reset decimal place counter
            }
        }

        // Finally process any remaining operation
        switch (operator) {
            case '*':
                result *= currentNumber;
                break;
            case '/':
                if (currentNumber != 0) {
                    result /= currentNumber;
                } else {
                    return "Can't divide by zero";
                }
                break;
            case '+':
                result += currentNumber;
                break;
            case '-':
                result -= currentNumber;
                break;
            case '%':
                if (currentNumber > 0) {
                    result += result * (currentNumber / 100);
                }
                break;
        }

        return (result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result);
    }
}