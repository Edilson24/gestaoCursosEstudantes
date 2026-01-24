package org.example.candidavagasjavafx.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.candidavagasjavafx.config.Conexao;
import org.example.candidavagasjavafx.entidades.Candidato;
import org.example.candidavagasjavafx.entidades.HoldDatas;
import org.example.candidavagasjavafx.entidades.Vaga;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import java.io.File;


public class DashboardFuncionarioController implements Initializable {

    Vaga vaga;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button MINUS;

    @FXML
    private Button candidaturas_btn;

    @FXML
    private Button close;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private BarChart<?, ?> home_graficoCandidato;

    @FXML
    private ComboBox<String> inscricao_curso;

    @FXML
    private LineChart<?, ?> home_graficoVagas;

    @FXML
    private Label home_totalCandidatos;

    @FXML
    private Label home_totalVagas;

    @FXML
    private Label home_totalCursos;

    @FXML
    private Button novo_btn;

    @FXML
    private Button eliminar_candidatoBtn;

    @FXML
    private Button sair_btn;

    @FXML
    private Button vagas_btn;

    @FXML
    private AnchorPane vagas_form;

    @FXML
    private AnchorPane candidatura_form;

    @FXML
    private AnchorPane inscricao_form;

    @FXML
    private TextField vagas_area;

    @FXML
    private TextField inscrica_nome;

    @FXML
    private TextField inscricao_idade;

    @FXML
    private Button vagas_atualizarBtn;

    @FXML
    private TableView<Vaga> vagas_tableView;

    @FXML
    private TableColumn<Vaga, String> vagas_col_area;

    @FXML
    private TableColumn<Vaga, String> vagas_col_pagamento;

    @FXML
    private TableColumn<Vaga, String> vagas_col_vagas;

    @FXML
    private TableColumn<Vaga, String> vagas_col_abertura;

    @FXML
    private TableColumn<Vaga, String> vagas_col_encerramento;

    @FXML
    private Button vagas_eliminarBtn;

    @FXML
    private DatePicker vagas_dataAbertura;

    @FXML
    private Button vagas_inserirBtn;

    @FXML
    private Button vagas_limparBtn;

    @FXML
    private TextField vagas_vaga;

    @FXML
    private DatePicker vagas_dataEncerramento;

    @FXML
    private TextField vagas_valor;

    @FXML
    private Label username;

    @FXML
    private Label inscricao_labelValor;

    @FXML
    private ListView<Vaga> home_vagasList;

    @FXML
    private ListView<Vaga> inscricaoList;

    @FXML
    private ListView<Candidato> candidato_ListView;

    @FXML
    private Circle imagemPerfil;



    public void cadastrarCandidato(){
        String sql = "INSERT INTO candidatos (nome, idade, vaga, pagamento, data) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql)){

            Alert alert;

            if (inscrica_nome.getText().isEmpty()
                    ||  inscricao_idade.getText().isEmpty()
                    ||  inscricao_labelValor.getText() == "0"
                    ||  inscricao_curso.getSelectionModel() == null

            ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MENSAGEM DE ERRO");
                alert.setContentText("PREENCHA OS CAMPOS EM BRANCO");
                alert.setHeaderText(null);
                alert.showAndWait();
            }else {
                prepare.setString(1, inscrica_nome.getText().trim());
                prepare.setString(2, inscricao_idade.getText().trim());
                prepare.setString(3, inscricao_curso.getSelectionModel().getSelectedItem());
                prepare.setString(4, inscricao_labelValor.getText().trim());

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(5, String.valueOf(sqlDate));

                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MENSAGEM DE CONFIRMAÇÃO");
                alert.setContentText("ESTUDANTE INSCRITO COM SUCESSO");
                alert.setHeaderText(null);
                alert.showAndWait();

                mostrarListaCandidatosListView();
                maisUmCandidato(inscricao_curso.getSelectionModel().getSelectedItem());
                configurarListView();
                mostrarListaVagasListView();
                limparCamposInscricao();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void maisUmCandidato(String curso) {

        int vagasDisponiveis = 0;
        int candidatosInscritos = 0;

        String selectSQL = "SELECT vagas, inscritos FROM vagas WHERE area = ?";
        String updateSQL = "UPDATE vagas SET vagas = ?, inscritos = ? WHERE area = ?";

        try (Connection connect = Conexao.obterConexao()) {

            // 1️⃣ Buscar dados atuais
            try (PreparedStatement psSelect = connect.prepareStatement(selectSQL)) {
                psSelect.setString(1, curso);

                ResultSet rs = psSelect.executeQuery();

                if (rs.next()) {
                    vagasDisponiveis = rs.getInt("vagas");
                    candidatosInscritos = rs.getInt("inscritos");
                } else {
                    System.out.println("Curso não encontrado!");
                    return;
                }
            }

            // (opcional) validar vagas
            if (vagasDisponiveis <= 0) {
                System.out.println("Não há vagas disponíveis!");
                return;
            }

            // 2️⃣ Atualizar dados
            try (PreparedStatement psUpdate = connect.prepareStatement(updateSQL)) {
                psUpdate.setInt(1, vagasDisponiveis - 1);
                psUpdate.setInt(2, candidatosInscritos + 1);
                psUpdate.setString(3, curso);

                psUpdate.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limparCamposInscricao(){
        inscrica_nome.setText("");
        inscricao_idade.setText("");
        inscricao_curso.getSelectionModel().clearSelection();
        inscricao_labelValor.setText("0");
    }

    public void acharValorCurso(){
        String sql = "SELECT valor FROM vagas WHERE area = '" + inscricao_curso.getSelectionModel().getSelectedItem() + "'";

        try(Connection connect = Conexao.obterConexao();
            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery())
        {
            if (result.next()){
                float valor = result.getFloat("valor");
                inscricao_labelValor.setText(String.valueOf(valor));
            }

            configurarListViewCandidato();
            mostrarListaCandidatosListView();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void homeGraficoVagas(){
        home_graficoVagas.getData().clear();

        String sql = "SELECT data, COUNT(idvagas) FROM vagas GROUP BY data ORDER BY TIMESTAMP(data) ASC LIMIT 7";


        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery())
        {

            XYChart.Series grafico = new XYChart.Series();



            while (result.next()){
                grafico.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_graficoVagas.getData().add(grafico);

        } catch (Exception e) {e.printStackTrace();}
    }

    public void homeGrafico(){
        home_graficoCandidato.getData().clear();

        String sql = "SELECT data, COUNT(idcandidato) FROM candidatos GROUP BY data ORDER BY TIMESTAMP(data) ASC LIMIT 7";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery();)
        {
            XYChart.Series grafico = new XYChart.Series();

            while (result.next()){
                grafico.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_graficoCandidato.getData().add(grafico);

        } catch (Exception e) {e.printStackTrace();}
    }

    public void somaCandidatos(){
        String sql = "SELECT * FROM candidatos";

        try (Connection connct = Conexao.obterConexao();
             PreparedStatement prepare = connct.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            int acomulador = 0;
            while (result.next()){
                String repetidor = result.getString("nome");
                acomulador += 1;
            }

            home_totalCandidatos.setText(String.valueOf(acomulador));
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inscricaoGetCursoDisponivel(){
        String sql = "SELECT * FROM vagas WHERE vagas > inscritos";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet resul = prepare.executeQuery()){

            ObservableList listaCurso = FXCollections.observableArrayList();

            while (resul.next()){
                listaCurso.add(resul.getString("area"));
            }

            inscricao_curso.setItems(listaCurso);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void somaVagas(){
        String sql = "SELECT vagas FROM vagas";

        try (Connection connct = Conexao.obterConexao();
             PreparedStatement prepare = connct.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            int acomulador = 0;
            while (result.next()){
                int repetidor = result.getInt("vagas");
                acomulador += repetidor;
            }

            home_totalVagas.setText(String.valueOf(acomulador));
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void configurarListViewCurso(){
        inscricaoList.setCellFactory(list -> new ListCell<>() {

            @Override
            protected void updateItem(Vaga vaga, boolean empty) {
                super.updateItem(vaga, empty);

                if (empty || vaga == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    Label lblArea = new Label(vaga.getArea());
                    lblArea.getStyleClass().add("vaga-area");

                    Label lblVagas = new Label("Vagas        : " + vaga.getVagas());
                    Label lblInscritos = new Label("Inscritoos  : " + vaga.getInscritos());
                    Label lblValor = new Label("Valor          : " + vaga.getValor());

                    VBox box = new VBox(lblArea, lblVagas, lblInscritos, lblValor);
                    box.getStyleClass().add("vaga-card");
                    box.setSpacing(2);

                    box.setOpacity(0);
                    box.setTranslateY(10);

                    FadeTransition fade = new FadeTransition(Duration.millis(250), box);
                    fade.setFromValue(0);
                    fade.setToValue(1);

                    TranslateTransition slide = new TranslateTransition(Duration.millis(270), box);
                    slide.setFromY(10);
                    slide.setToY(0);

                    new ParallelTransition(fade, slide).play();

                    setGraphic(box);
                    inscricaoList.setFixedCellSize(130);
                }
            }
        });
    }


    public void configurarListView() {

        home_vagasList.setCellFactory(list -> new ListCell<>() {

            @Override
            protected void updateItem(Vaga vaga, boolean empty) {
                super.updateItem(vaga, empty);

                if (empty || vaga == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    Label lblArea = new Label(vaga.getArea());
                    lblArea.getStyleClass().add("vaga-area");

                    Label lblVagas = new Label("Vagas: " + vaga.getVagas());
                    Label lblValor = new Label("Valor: " + vaga.getValor());

                    VBox box = new VBox(lblArea, lblVagas, lblValor);
                    box.getStyleClass().add("vaga-card");
                    box.setSpacing(6);

                    box.setOpacity(0);
                    box.setTranslateY(10);

                    FadeTransition fade = new FadeTransition(Duration.millis(250), box);
                    fade.setFromValue(0);
                    fade.setToValue(1);

                    TranslateTransition slide = new TranslateTransition(Duration.millis(270), box);
                    slide.setFromY(10);
                    slide.setToY(0);

                    new ParallelTransition(fade, slide).play();

                    setGraphic(box);
                    home_vagasList.setFixedCellSize(120);

                }
            }
        });
    }



    public ObservableList<Vaga> ListaVagasDisponiveis(){
        ObservableList <Vaga> listaVagas = FXCollections.observableArrayList();

        String sql = "SELECT * FROM vagas";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()
        ){
            Vaga vaga;

            while (result.next()){
                vaga = new Vaga(
                        result.getInt("idvagas"),
                        result.getString("area"),
                        result.getInt("vagas"),
                        result.getInt("inscritos"),
                        result.getDate("abertura"),
                        result.getDate("encerramento"),
                        result.getDouble("valor"),
                        result.getDate("data"));
                listaVagas.add(vaga);
            }
        } catch (Exception e) {e.printStackTrace();}
        return listaVagas;
    }

    private ObservableList <Vaga> ListaVagas;
    public void mostrarListaVagas(){
        ListaVagas = ListaVagasDisponiveis();

        vagas_col_area.setCellValueFactory(new PropertyValueFactory<>("area"));
        vagas_col_vagas.setCellValueFactory(new PropertyValueFactory<>("vagas"));
        vagas_col_abertura.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
        vagas_col_encerramento.setCellValueFactory(new PropertyValueFactory<>("dataEncerramento"));
        vagas_col_pagamento.setCellValueFactory(new PropertyValueFactory<>("valor"));

        vagas_tableView.setItems(ListaVagas);
    }

    //AREA DE CANDIDATOS
    public ObservableList <Candidato> BuscaListaCandidatos(){
        ObservableList <Candidato> listaCandidato = FXCollections.observableArrayList();

        String sql = "SELECT * FROM candidatos";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()
        ){
            Candidato candidato;

            while (result.next()){
                candidato = new Candidato(
                        result.getInt("idcandidato"),
                        result.getString("nome"),
                        result.getInt("idade"),
                        result.getString("vaga"),
                        result.getFloat("pagamento"));
                listaCandidato.add(candidato);
            }
        } catch (Exception e) {e.printStackTrace();}
        return listaCandidato;
    }

    //LISTVIEW EM CONSTRUCAO
    public void configurarListViewCandidato(){
        candidato_ListView.setCellFactory(lista -> new ListCell<>() {

            @Override
            protected void updateItem(Candidato candidato, boolean empty) {
                super.updateItem(candidato, empty);

                if (empty || candidato == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    Label lblNome = new Label(candidato.getNome());
                    lblNome.getStyleClass().add("candidato-area");

                    Label lblVagas = new Label(candidato.getCurso());

                    VBox box = new VBox(lblNome, lblVagas);
                    box.getStyleClass().add("candidato-card");
                    box.setSpacing(6);

                    box.setOpacity(0);
                    box.setTranslateY(10);

                    FadeTransition fade = new FadeTransition(Duration.millis(250), box);
                    fade.setFromValue(0);
                    fade.setToValue(1);

                    TranslateTransition slide = new TranslateTransition(Duration.millis(270), box);
                    slide.setFromY(10);
                    slide.setToY(0);

                    new ParallelTransition(fade, slide).play();

                    setGraphic(box);
                    candidato_ListView.setFixedCellSize(90);
                }
            }
        });
    }

    public void mostrarListaVagasListView() {
        home_vagasList.setItems(ListaVagasDisponiveis());
        inscricaoList.setItems(ListaVagasDisponiveis());
    }

    public void mostrarListaCandidatosListView() {
        candidato_ListView.setItems(BuscaListaCandidatos());
    }

    public void somaCursos(){
        String sql = "SELECT COUNT(idvagas) FROM vagas";

        int contagemDados = 0;

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery();) {

            while (result.next()){
                contagemDados = result.getInt("COUNT(idvagas)");
            }
            home_totalCursos.setText(String.valueOf(contagemDados));
        } catch (Exception e) {e.printStackTrace();}

    }

    int variavelGuardaIdCandidatos = 0;
    public void selecionarVagaListView() {
        candidato_ListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, candidato) -> {

            if (candidato != null) {
                inscrica_nome.setText(candidato.getNome());
                inscricao_idade.setText(String.valueOf(candidato.getIdade()));
                inscricao_labelValor.setText(String.valueOf(candidato.getPagamento()));
                inscricao_curso.setValue(candidato.getCurso());
            }
        });
    }

    public void mudarTela(ActionEvent event){
        if (event.getSource() == home_btn){
            home_form.setVisible(true);
            inscricao_form.setVisible(false);

            configurarListView();
            mostrarListaVagasListView();
            somaVagas();
            somaCursos();
            somaCandidatos();

            homeGraficoVagas();
            homeGrafico();
        }

        else if (event.getSource() == candidaturas_btn) {
            home_form.setVisible(false);
            inscricao_form.setVisible(true);

            inscricaoGetCursoDisponivel();
            configurarListViewCurso();
            configurarListViewCandidato();

            mostrarListaCandidatosListView();
            selecionarVagaListView();
        }
    }

    public void displayUsuario(){
        String user = HoldDatas.username;

        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));

        String sql = "SELECT * FROM usuario WHERE nome = ?";

        try (Connection connect = Conexao.obterConexao();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, username.getText());

            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    String caminhoImagem = result.getString("imagem");

                    Image image;

                    try {
                        File file = new File(caminhoImagem);
                        image = new Image(file.toURI().toString());
                    } catch (Exception e) {
                        // imagem padrão caso dê erro
                        image = new Image(
                                getClass().getResource("/imagens/default.png").toExternalForm()
                        );
                    }
                    imagemPerfil.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void minimizarJanela() {
        Stage stage = (Stage) MINUS.getScene().getWindow();
        stage.setIconified(true);
    }

    private double x = 0;
    private double y = 0;
    //SAIR DA DASHBOARD PARA TELA DE LOGIN
    public void sair() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRME");
        alert.setHeaderText(null);
        alert.setContentText("TEM CERTEZA QUE DESEJA SAIR?");
        Optional<ButtonType> optional = alert.showAndWait();

        if (optional.get().equals(ButtonType.OK)){

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/candidavagasjavafx/views/login.fxml"));
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
            } catch (Exception e) {
            }
        }


    }

    public void close(){
        System.exit(0);
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

        displayUsuario();

        somaVagas();
        somaCursos();
        somaCandidatos();
        configurarListView();
        mostrarListaVagasListView();

        homeGrafico();
        homeGraficoVagas();

        inscricaoGetCursoDisponivel();
        configurarListViewCurso();
        configurarListViewCandidato();

        mostrarListaCandidatosListView();

        selecionarVagaListView();

    }
}
