package com.example.encryption_intarfase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class HelloController {
    @FXML
    private String user_VIBOR = "";
    private String user_ADRESS = "";
    private String answerServer = "";
    @FXML
    private RadioButton Rashifrovka;

    @FXML
    private Button Start_Change;

    @FXML
    private TextField User_Path_Adress;

    @FXML
    private RadioButton Zashifrovka;

    @FXML
    private Text ReSULT_OPERATION_GET_USER;

    @FXML
    void  initialize () {


        Start_Change.setOnAction(actionEvent -> {main();ReSULT_OPERATION_GET_USER.setText(answerServer);user_VIBOR="";user_ADRESS ="";});


        User_Path_Adress.setOnAction(actionEvent -> {user_ADRESS=User_Path_Adress.getText();System.out.println(user_ADRESS);});


        Rashifrovka.setOnAction(actionEvent -> {if (Rashifrovka.isSelected()){user_VIBOR= "Rashifrovka";}else {user_VIBOR= "Zashifrovka";}System.out.println(user_VIBOR);});


        Zashifrovka.setOnAction(actionEvent -> {if (Zashifrovka.isSelected()){user_VIBOR="Zashifrovka";}else {user_VIBOR= "Rashifrovka";}System.out.println(user_VIBOR);});}

        public void main () {

            try  {
                String adr_read = user_ADRESS;
                if (adr_read.length() < 3) {
                    answerServer="Ваш путь до файла указан неккоректно, узнать путь можно так -> (файл-пкм-свойства-безопасность)";
                    return;
                }

                String result = Files.readString(Path.of(adr_read));
                if (user_VIBOR.equals("Rashifrovka")) {
                    Encryption encryption = new Encryption(result);
                    String get_File = encryption.method_Decryption_File();
                    if (get_File.equals("В тексте не найдено ключей.Расшифровка невозможна")) {
                        answerServer = "В тексте не найдено ключей для расшифровки. Расшифровка невозможна";
                    } else {
                        File myFile = new File("get_File_myfile_Decryption.txt");
                        FileOutputStream outputStream = new FileOutputStream(myFile);
                        byte[] buffer = get_File.getBytes();
                        outputStream.write(buffer);
                        outputStream.close();
                        answerServer = "Файл сохранен в корне программы, название файла get_File_myfile_Decryption.txt";
                    }

                } else if (user_VIBOR.equals("Zashifrovka")) {

                    Encryption encryption = new Encryption(result);
                    String get_File = encryption.method_Encryption();
                    if (get_File.equals("Ограничение по длине текста- минимум 20 символов")) {
                        answerServer = "Ограничение по колличеству символов в файле- минимум 20, нужен файл более 20 символов";
                    } else {
                        File myFile = new File("get_File_myfile_encryption.txt");
                        FileOutputStream outputStream = new FileOutputStream(myFile);
                        byte[] buffer = get_File.getBytes();
                        outputStream.write(buffer);
                        outputStream.close();
                        answerServer = "Файл сохранен в корне программы, название файла get_File_myfile_encryption.txt";
                    }

                }

            } catch (IOException e) {
                answerServer = "Расширение файла или путь к файлу указан неккоректно";
                throw new RuntimeException("Расширение файла или путь к файлу указан неккоректно");

            }


        }
    static class Encryption {
        private static int getRandom(int min, int max) {
            int range = (max - min) + 1;
            int random = (int) ((range * Math.random()) + min);
            return random;
        }

        private static final int CONST = 20;
        private String text;
        private static final String KEY_SEPARATOR = ":_<";
        private static final String SEPARATOR = "'@>";

        public String getText() {
            return text;
        }

        public Encryption(String text) {
            this.text = text;
        }

        public String method_Encryption() {
            if (text.length() < 20) {
                return new String("Ограничение по длине текста- минимум 20 символов");
            }
            byte key_1, key_2, key_3;
            key_1 = (byte) getRandom(1, 9);
            key_2 = (byte) getRandom(10, 99);
            key_3 = (byte) getRandom(100, 127);
            StringBuilder secret_file = new StringBuilder();
            byte counter = 1;
            secret_file = secret_file.append(KEY_SEPARATOR + SEPARATOR + key_1 +
                    SEPARATOR + key_2 + SEPARATOR + key_3 + SEPARATOR + KEY_SEPARATOR);
            for (int i = 0; i < text.length(); i++) {
                if (i % CONST == 0) {
                    counter++;
                }
                if (counter == 1) {
                    secret_file = secret_file.append((char) (text.charAt(i) + key_1));

                } else if (counter == 2) {
                    secret_file = secret_file.append((char) (text.charAt(i) + key_2));
                } else if (counter == 3) {
                    secret_file = secret_file.append((char) (text.charAt(i) + key_3));
                }
                counter = 1;
            }
            return secret_file.toString();
        }

        public String method_Decryption_File() {
            String[] find_key = text.split(KEY_SEPARATOR);
            if (find_key.length < 2) {
                return new String("В тексте не найдено ключей.Расшифровка невозможна");
            }
            String[] get_Key = text.split(SEPARATOR);

            int key_1, key_2, key_3;
            key_1 = Integer.parseInt(get_Key[1]);
            key_2 = Integer.parseInt(get_Key[2]);
            key_3 = Integer.parseInt(get_Key[3]);
            int counter = 1;
            String a = text.substring(24);
            StringBuilder secret_file = new StringBuilder();
            for (int i = 0; i < a.length(); i++) {
                if (i % CONST == 0) {
                    counter++;
                }
                if (counter == 1) {
                    secret_file = secret_file.append((char) (a.charAt(i) - key_1));

                } else if (counter == 2) {
                    secret_file = secret_file.append((char) (a.charAt(i) - key_2));
                } else if (counter == 3) {
                    secret_file = secret_file.append((char) (a.charAt(i) - key_3));
                }
                counter = 1;
            }
            return secret_file.toString();

        }

    }

    }


