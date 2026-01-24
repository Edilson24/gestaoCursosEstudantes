package org.example.candidavagasjavafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.candidavagasjavafx.config.Conexao;
import org.example.candidavagasjavafx.entidades.HoldDatas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private Button close;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField usuario;

    private double x = 0;
    private double y = 0;

    public void loginAdmin(){

        String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ?";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
        ){
            prepare.setString(1, usuario.getText().trim());
            prepare.setString(2, password.getText().trim());

            try (ResultSet result = prepare.executeQuery()){
                Alert alert;

                if (usuario.getText().isEmpty() || password.getText().isEmpty()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("PREENCHA TODOS OS CAMPOS EM BRANCO");
                    alert.showAndWait();
                }else {

                    if (result.next()){
                        String prioridade = result.getString("prioridade");

                        HoldDatas.username = usuario.getText();

                        if (prioridade.equals("administrador")){
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("ACESSO CONCEDIDO AO ADMINISTRADOR");
                            alert.showAndWait();

                            //ABRIR DASHBOARD DO ADMINISTRADOR
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/candidavagasjavafx/views/dashboardAdministrador.fxml"));
                            Parent root = loader.load();

                            Stage stage = new Stage();
                            Scene scene = new Scene(root);

                            stage.initStyle(StageStyle.TRANSPARENT);

                            root.setOnMousePressed((MouseEvent event) ->{
                                x = event.getSceneX();
                                y = event.getSceneY();
                            });

                            root.setOnMouseDragged((MouseEvent event) ->{
                                stage.setX(event.getScreenX() - x);
                                stage.setY(event.getScreenY() - y);

                                stage.setOpacity(.8);
                            });

                            root.setOnMouseReleased((MouseEvent event) ->{
                                stage.setOpacity(1);
                            });

                            stage.setScene(scene);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.setTitle("Painel de Controle - Sistema de Candidaturas");
                            stage.setResizable(false);
                            stage.show();

                            // Fecha a janela de login
                            Stage loginStage = (Stage) main_form.getScene().getWindow();
                            loginStage.close();


                        }else if (prioridade.equals("funcionario")){
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("ACESSO CONCEDIDO AO FUNCIONARIO");
                            alert.showAndWait();

                            //ABRIR DASHBOARD DO FUNCIONARIO
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/candidavagasjavafx/views/dashboardFuncionario.fxml"));
                            Parent root = loader.load();

                            Stage stage = new Stage();
                            Scene scene = new Scene(root);

                            stage.initStyle(StageStyle.TRANSPARENT);

                            root.setOnMousePressed((MouseEvent event) ->{
                                x = event.getSceneX();
                                y = event.getSceneY();
                            });

                            root.setOnMouseDragged((MouseEvent event) ->{
                                stage.setX(event.getScreenX() - x);
                                stage.setY(event.getScreenY() - y);

                                stage.setOpacity(.8);
                            });

                            root.setOnMouseReleased((MouseEvent event) ->{
                                stage.setOpacity(1);
                            });

                            stage.setScene(scene);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.setTitle("Painel de Controle - Sistema de Candidaturas");
                            stage.setResizable(false);
                            stage.show();

                            // Fecha a janela de login
                            Stage loginStage = (Stage) main_form.getScene().getWindow();
                            loginStage.close();

                        }else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("CARGO NAO ENCONTRADO");
                            alert.showAndWait();
                        }
                    }else{
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("ACESSO NEGADO - CREDENCIAIS NAO RECONHECIDAS");
                        alert.showAndWait();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        System.exit(0);
    }
}

