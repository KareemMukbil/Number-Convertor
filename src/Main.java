import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application{
    public static void main(String[] args){
        launch(Main.class);
    }


    @Override
    public void start(Stage stage){
        GridPane layout = new GridPane();

        Label convertFrom = new Label("Convert From: (decimal, hexadecimal or binary)");
        TextField convertFromText = new TextField();

        Label convertTo = new Label("Convert To: (decimal, hexadecimal or binary)");
        TextField convertToText = new TextField();

        Label number = new Label("Your Number: ");
        TextField numberText = new TextField();

        Label answer = new Label();
        Label theOptions = new Label();
        Button erase = new Button("Erase");
        Button otherOptions = new Button("Show Other Conversions");
        Button hideOtherOptions = new Button("Hide Other Conversions");
        Button restart = new Button("Restart");
        Button start = new Button("Convert");

        layout.add(convertFrom,0,0);
        layout.add(convertFromText,0,1);
        layout.add(convertTo,0,2);
        layout.add(convertToText,0,3);
        layout.add(number,0,4);
        layout.add(numberText,0,5);
        layout.add(start,0,6);
        layout.add(erase,0,7);
        setupLayouts(layout);
        Scene homeScreen = new Scene(layout);

        GridPane answerScene = new GridPane();
        setupLayouts(answerScene);
        answerScene.add(answer,0,3);
        answerScene.add(otherOptions,3,9);
        answerScene.add(hideOtherOptions,3,9);
        answerScene.add(theOptions,0,4);
        theOptions.setVisible(false);
        hideOtherOptions.setVisible(false);
        answerScene.add(restart,0,9);
        Scene endScene = new Scene(answerScene,500,300);

        // Setting up the layouts, labels, and buttons



        start.setOnAction((actionEvent -> {
            if(convertFromText.getText().equalsIgnoreCase(convertToText.getText())){
                if(convert(numberText.getText(), convertFromText.getText(), convertToText.getText()).equals("Incorrect Format!")){
                    answer.setText("Incorrect Format!");
                    otherOptions.setVisible(false);
                    stage.setScene(endScene);
                }else {
                    answer.setText("Here is your result in " + convertToText.getText() + ": \n " + numberText.getText());
                    if(!otherOptions.isVisible()){
                        otherOptions.setVisible(true);
                    }
                    theOptions.setText(otherOptions(numberText.getText(), convertFromText.getText(), convertToText.getText()));
                    stage.setScene(endScene);
                }
            }else if(!convertFromText.getText().equalsIgnoreCase(convertToText.getText())){
                if(convert(numberText.getText(), convertFromText.getText(), convertToText.getText()).equals("Incorrect Format!")){
                    answer.setText("Incorrect Format!");
                    otherOptions.setVisible(false);
                    stage.setScene(endScene);
                }else{
                    answer.setText("Here is your result in " + convertToText.getText() + ": \n" + convert(numberText.getText(), convertFromText.getText(), convertToText.getText()));
                    if(!otherOptions.isVisible()){
                        otherOptions.setVisible(true);
                    }
                    theOptions.setText(otherOptions(numberText.getText(),convertFromText.getText(),convertToText.getText()));
                    stage.setScene(endScene);
                }
            }
        }));
        restart.setOnAction((actionEvent -> {
            otherOptions.setVisible(false);
            hideOtherOptions.setVisible(false);
            theOptions.setVisible(false);
            stage.setScene(homeScreen);
        }));
        erase.setOnAction((actionEvent -> {
            convertFromText.setText("");
            convertToText.setText("");
            numberText.setText("");
        }));
        otherOptions.setOnAction((actionEvent -> {
            theOptions.setVisible(true);
            otherOptions.setVisible(false);
            hideOtherOptions.setVisible(true);
        }));
        hideOtherOptions.setOnAction((actionEvent -> {
            theOptions.setVisible(false);
            otherOptions.setVisible(true);
            hideOtherOptions.setVisible(false);
        }));
        // Setting up the buttons, and what they do when clicked

        stage.setScene(homeScreen);
        stage.setTitle("Number Convertor");
        stage.show();
    }

    public static void setupLayouts(GridPane gridPane){
        gridPane.setPrefSize(300,180);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20,20,20,20));
        // Aesthetic
    }

    public String convert(String value1, String from, String to){

        ArrayList<String> list = new ArrayList<>();
        StringBuilder returnObject = new StringBuilder();
        // defines variables we will need later on

        boolean incorrectFormat = false;
        char[] containsAlphabet = value1.toCharArray();
        for(char i: containsAlphabet){
            if(Character.isAlphabetic(i) && !from.equalsIgnoreCase("hexadecimal")){
                incorrectFormat = true;
                break;
            }
        }
        // Checks if the inputted number contains an alphabetical character; if it does the code shows "Format Incorrect!"

        if(String.valueOf(value1.charAt(0)).equals("-")){
            StringBuilder newValue = new StringBuilder();
            for(char chars: containsAlphabet){
                if(String.valueOf(chars).equals("-")){
                    continue;
                }
                newValue.append(chars);
            }
            value1 = String.valueOf(newValue);
            returnObject.append("-");
        }
        // Checks if it's a negative number or not, and adds the negative sign in advance to the returnObject after removing it from value1. I do this not to mess with syntax.

        Pattern specialChar = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecialChar = specialChar.matcher(value1);
        if(hasSpecialChar.find() && !from.equalsIgnoreCase("hexadecimal")){
            return "Incorrect Format!";
        }
        // Checks if the inputted number contains a special character

        if(incorrectFormat){
            return "Incorrect Format!";
        }



        if(from.equalsIgnoreCase("decimal") && to.equalsIgnoreCase("binary")){
            int value = Integer.valueOf(value1);

            while(value > 0){
                if(value % 2 == 0){
                    list.add("0");
                }else{
                    list.add("1");
                }
                value = value/2;
            }
            Collections.reverse(list);
            for(String nums: list){
                returnObject.append(nums);
            }
            // Converts decimal to binary
        }else if(from.equalsIgnoreCase("decimal") && to.equalsIgnoreCase("hexadecimal")){
            int value = Integer.valueOf(value1);

            while(value > 0){
                int remainder = value % 16;
                if(remainder == 10){
                    list.add("A");
                    value = value/16;
                    continue;
                } else if (remainder == 11) {
                    list.add("B");
                    value = value/16;
                    continue;
                }else if (remainder == 12) {
                    list.add("C");
                    value = value/16;
                    continue;
                }else if (remainder == 13) {
                    list.add("D");
                    value = value/16;
                    continue;
                }else if (remainder == 14) {
                    list.add("E");
                    value = value/16;
                    continue;
                }else if (remainder == 15) {
                    list.add("F");
                    value = value/16;
                    continue;
                }
                list.add(String.valueOf(remainder));
                value = value/16;
            }
            Collections.reverse(list);
            for(String nums: list){
                returnObject.append(nums);
            }
            // Converts decimal to hexadecimal
        }else if(from.equalsIgnoreCase("binary") && to.equalsIgnoreCase("decimal")){
            for(int i = 0; i < value1.length(); i++){
                String[] newValue = value1.split("");
                if (Integer.valueOf(newValue[i]) > 1) {
                    incorrectFormat = true;
                    break;
                }
            }
            // Verifies format.

            int result = 0;
            int index = 0;
            while(value1.length() > index){
                String[] addition = value1.split("");
                result = (result * 2) +  Integer.valueOf(addition[index]);
                index++;
            }
            returnObject.append(result);
            // Converts binary to decimal
        }else if(from.equalsIgnoreCase("hexadecimal") && to.equalsIgnoreCase("decimal")){

            ArrayList<Integer> sumOfResults = new ArrayList<>();
            char[] chars = value1.toCharArray();
            int result = 0;
            int index = 0;
            while(value1.length() > index){
                String[] nums = value1.split("");
                result = hexadecimalEquivalent(nums[index]) * (int) Math.pow(16, value1.length() - index - 1);
                sumOfResults.add(result);
                index++;
            }
            int sum = 0;
            for(int add: sumOfResults){
                sum = add + sum;
            }
            returnObject.append(sum);
            // Converts hexadecimal to decimal

        }else if(from.equalsIgnoreCase("binary") && to.equalsIgnoreCase("hexadecimal")){
            String toDecimal = convert(value1,"binary","decimal");
            returnObject.append(convert(toDecimal,"decimal", "hexadecimal"));
            // Convert binary to decimal, and then decimal to hexadecimal. Easier than doing binary to hexadecimal directly
        }else if(from.equalsIgnoreCase("hexadecimal") && to.equalsIgnoreCase("binary")){
            String toDecimal = convert(value1,"hexadecimal","decimal");
            returnObject.append(convert(toDecimal,"decimal", "binary"));
            // Converts hexadecimal to decimal, and then decimal to binary. Easier than doing hexadecimal to binary directly
        }

        if(incorrectFormat){
            return "Incorrect Format!";
        }
        return returnObject.toString();

    }

    public int hexadecimalEquivalent(String a){
        int returnObject = 0;

        if(a.equals("A")){
            returnObject = 10;
        }else if(a.equals("B")){
            returnObject = 11;
        }else if(a.equals("C")){
            returnObject = 12;
        }else if(a.equals("D")){
            returnObject = 13;
        }else if(a.equals("E")){
            returnObject = 14;
        }else if(a.equals("F")){
            returnObject = 15;
        }else if(Integer.valueOf(a) < 16){
            returnObject = Integer.valueOf(a);
        }
        return returnObject;
        // Looks for the equivalent Hexadecimal alphabetical character
    }
    public String otherOptions(String value, String from, String to){
        String returnObject = "";

        if(from.equals("decimal") && to.equals("binary")){
            returnObject = "Here is your hexadecimal result: " + "\n" + convert(value, "decimal", "hexadecimal") + "\n" + "You put " + value + " in decimal";
        }else if(from.equals("decimal") && to.equals("hexadecimal")){
            returnObject = "Here is your binary result: " + "\n" + convert(value, "decimal", "binary") + "\n" + "You put " + value + " in decimal";
        }else if(from.equals("binary") && to.equals("decimal")){
            returnObject = "Here is your hexadecimal result: " + "\n" + convert(value, "binary", "hexadecimal") + "\n" + "You put " + value + " in binary";
        }else if(from.equals("binary") && to.equals("hexadecimal")){
            returnObject = "Here is your decimal result: " + "\n" + convert(value, "binary", "decimal") + "\n" + "You put " + value + " in binary";
        }else if(from.equals("hexadecimal") && to.equals("decimal")){
            returnObject = "Here is your binary result: " + "\n" + convert(value, "hexadecimal", "binary") + "\n" + "You put " + value + " in hexadecimal";
        }else if(from.equals("hexadecimal") && to.equals("binary")){
            returnObject = "Here is your decimal result: " + "\n" + convert(value, "hexadecimal", "decimal") + "\n" + "You put " + value + " in hexadecimal";
        }else if(from.equals("decimal") && to.equals("decimal")){
            returnObject = "Here is your binary result: " + "\n" + convert(value, "decimal", "binary") + "\n" + "Here is your result in hexadecimal: " + "\n" + convert(value, "decimal", "hexadecimal") + "\n" +"You put " + value + " in decimal";
        }else if(from.equals("binary") && to.equals("binary")){
            returnObject = "Here is your decimal result: " + "\n" + convert(value, "binary", "decimal") + "\n" + "Here is your hexadecimal result: " + "\n" + convert(value, "binary", "hexadecimal") + "\n" + "You put " + value + " in binary";
        }else if(from.equals("hexadecimal") && to.equals("hexadecimal")){
            returnObject = "Here is your binary result: " + "\n" + convert(value, "hexadecimal", "binary") + "\n" + "Here is your decimal result: " + "\n" + convert(value, "hexadecimal", "decimal") + "\n" + "You put " + value + " in hexadecimal";
        }
        return returnObject;
    }
    // Converts the user input to other conversions in case he presses "Other conversions"
}