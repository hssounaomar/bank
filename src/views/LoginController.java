/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dao.AdminDAO;
import entites.Admin;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.NoResultException;

/**
 * FXML Controller class
 *
 * @author Ideal-Info
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;
    @FXML
    private StackPane stackPane;
    private final AdminDAO dao = new AdminDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void connectPersonne(ActionEvent e) {
        try {

            Admin per = dao.login(email.getText(), password.getText());

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(this.getClass().getResource("Menu.fxml").openStream());

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (NoResultException ex) {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            content.setHeading(new Text("Votre Champs sont faux"));
            Text text = new Text("Il faut repeter la connexion vos champs sont faux.");
            text.setFill(Color.RED);

            content.setBody(text);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            JFXButton button = new JFXButton("OK");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    dialog.close();
                }
            });
            content.setActions(button);

            dialog.show();

        } catch (IOException ioe) {

        }
    }

}
