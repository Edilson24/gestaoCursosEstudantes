package org.example.candidavagasjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.candidavagasjavafx.config.Conexao;
import org.example.candidavagasjavafx.entidades.HoldDatas;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NovoUsuarioController implements Initializable {
    @FXML
    private Button cadastrarBtn;

    @FXML
    private Button close;

    @FXML
    private Button imoirtarImagemBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField confirmeSenha;

    @FXML
    private TextField nome;

    @FXML
    private ComboBox<?> posicao;

    @FXML
    private ImageView ImagemUsuario;

    @FXML
    private Image image;

    @FXML
    private PasswordField senha;

    public void availableCarImportImage(){

        FileChooser open = new FileChooser();
        open.setTitle("Abrir um ficheiro de iamge");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("image file", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null){
            HoldDatas.pathimage = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 200, 259, false, true);
            ImagemUsuario.setImage(image);
        }
    }

    public void cadastrar(){
        String sql = "INSERT INTO usuario (nome, senha, prioridade, imagem) VALUES (?, ?, ?, ?)";

        try (     Connection connect= Conexao.obterConexao();
                  PreparedStatement prepare = connect.prepareStatement(sql)){

            Alert alert;

            if (nome.getText().isEmpty()
                || senha.getText().isEmpty()
                || posicao.getSelectionModel().getSelectedItem() == null
                || HoldDatas.pathimage == null || HoldDatas.pathimage == "")
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MENSAGEM DE ERRO");
                alert.setContentText("PREENCHA TODOS OS CAMPOS VAZIOS");
                alert.setHeaderText(null);
                alert.showAndWait();

            }else {

                prepare.setString(1, nome.getText().trim());
                prepare.setString(2, senha.getText().trim());
                prepare.setString(3, (String) posicao.getSelectionModel().getSelectedItem());

                String uri = HoldDatas.pathimage;
                uri = uri.replace("\\", "\\\\");
                prepare.setString(4, uri);

                int linha = prepare.executeUpdate();

                if (linha > 0){
                    System.out.println("SUCESSO");
                }

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MENSAGEM DE CONFIRMAÇÃO");
                alert.setContentText("NOVO USUARIO CADASTRADO");
                alert.setHeaderText(null);
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] listPosicao = {"administrador", "funcionario"};
    public void comboPosicao(){
        List<String> listaEst = new ArrayList<>();

        for (String data: listPosicao){
            listaEst.add(data);
        }

        ObservableList listaData = FXCollections.observableArrayList(listaEst);
        posicao.setItems(listaData);
    }

    public void close(){

        Stage loginStage = (Stage) main_form.getScene().getWindow();
        loginStage.close();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboPosicao();
    }
}
