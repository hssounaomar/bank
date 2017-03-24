/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import bank.exceptions.IllegalOrphanException;
import bank.exceptions.NonexistentEntityException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import dao.CompteDAO;
import entites.Compte;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ideal-Info
 */
public class GererComptesController implements Initializable {

    @FXML
    private JFXButton btn_ajouter;
    @FXML
    private JFXButton btn_supprimer;
    @FXML
    private JFXButton btn_modifier;
    @FXML
    private JFXTextField text_num;
    @FXML
    private JFXTextField text_nom;
    @FXML
    private JFXTextField text_prenom;
    @FXML
    private JFXTextField text_solde;
    @FXML
    private TableView<Compte> table;
    @FXML
    private TableColumn<Compte, Integer> col_num;
    @FXML
    private TableColumn<Compte, String> col_nom;
    @FXML
    private TableColumn<Compte, String> col_prenom;
    @FXML
    private TableColumn<Compte, Float> col_solde;
    CompteDAO dao = new CompteDAO();
    private ObservableList<Compte> comptes_list = FXCollections.observableArrayList(dao.getComptes());
private StackPane stackPane=new StackPane();
    @FXML
    private Pane pane;
    @FXML
    private JFXTextField text_recherche;
    
    public void initialize(URL url, ResourceBundle rb) {
       
        col_num.setCellValueFactory(new PropertyValueFactory<Compte, Integer>("Id"));
        col_nom.setCellValueFactory(new PropertyValueFactory<Compte, String>("Nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<Compte, String>("Prenom"));
        col_solde.setCellValueFactory(new PropertyValueFactory<Compte, Float>("Solde"));
        table.setItems(comptes_list);
filterComptes();
        table.setRowFactory(tv -> {
            TableRow<Compte> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {

                    Compte compte = row.getItem();
                    text_nom.setText(compte.getNom());

                    text_num.setText(compte.getId().toString());
                    text_num.setDisable(true);
                    text_prenom.setText(compte.getPrenom());
                    text_solde.setText(Float.toString(compte.getSolde()));

                }
            });
            return row;
        });

    }

    @FXML
    public void ajouterCompte(ActionEvent e) {
        if ((text_nom.getText().length()>0) && (text_prenom.getText().length()>0) && (text_solde.getText().length()>0)) {
              JFXDialogLayout content = new JFXDialogLayout();
              
            content.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            content.setHeading(new Text("Ajouter un compte"));
            Text text = new Text("Est-ce que vous voulez vraiement ajouter ce compte de "+text_nom.getText()+" ?");
            text.setFill(Color.RED);
  
            content.setBody(text);
            stackPane.setPrefHeight(150);
            stackPane.setPrefWidth(150);
            
          pane.getChildren().add(stackPane);
          stackPane.setLayoutX(175);
          stackPane.setLayoutY(150);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            JFXButton buttonOK = new JFXButton("OK");
            buttonOK.setOnAction(((event) -> {
                          //Ajouter un compte avec refresh de table
            Compte comp = new Compte();
            comp.setNom(text_nom.getText());
            comp.setPrenom(text_prenom.getText());
            comp.setSolde(Float.parseFloat(text_solde.getText()));
            dao.addCompte(comp);
            comptes_list.clear();
            comptes_list.addAll(dao.getComptes());
            clearTexts();
            dialog.close();
            pane.getChildren().remove(stackPane);
            }));
            JFXButton buttonNO =new JFXButton("NO");
            buttonNO.setOnAction(((event) -> {
                dialog.close();
pane.getChildren().remove(stackPane);            }));
            content.setActions(buttonOK,buttonNO);

            dialog.show();

           

        }

    }

    @FXML
    public void modifierCompte(ActionEvent e) throws Exception {
        if ((text_nom.getText().length()>0) && (text_prenom.getText().length()>0) && (text_solde.getText().length()>0)) {
            JFXDialogLayout content = new JFXDialogLayout();
              
            content.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            content.setHeading(new Text("Ajouter un compte"));
            Text text = new Text("Est-ce que vous voulez vraiement modifier votre compte monsieur "+text_nom.getText()+" ?");
            text.setFill(Color.RED);
  
            content.setBody(text);
            stackPane.setPrefHeight(150);
            stackPane.setPrefWidth(150);
            
          pane.getChildren().add(stackPane);
          stackPane.setLayoutX(175);
          stackPane.setLayoutY(150);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            JFXButton buttonOK = new JFXButton("OK");
            buttonOK.setOnAction(((event) -> {
                          //Ajouter un compte avec refresh de table
            Compte comp = new Compte();
            comp.setId(Integer.parseInt(text_num.getText()));
            comp.setNom(text_nom.getText());
            comp.setPrenom(text_prenom.getText());
            comp.setSolde(Float.parseFloat(text_solde.getText()));
                try {
                    dao.updateCompte(comp);
                } catch (Exception ex) {
                    Logger.getLogger(GererComptesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            comptes_list.clear();
            comptes_list.addAll(dao.getComptes());
            clearTexts();
            dialog.close();
            pane.getChildren().remove(stackPane);
            }));
            JFXButton buttonNO =new JFXButton("NO");
            buttonNO.setOnAction(((event) -> {
                dialog.close();
pane.getChildren().remove(stackPane);            }));
            content.setActions(buttonOK,buttonNO);

            dialog.show();
            
        }

    }

    @FXML
    public void supprimerCompte(ActionEvent e) throws IllegalOrphanException, NonexistentEntityException {
        if (text_num.getText().length()>0) {
             JFXDialogLayout content = new JFXDialogLayout();
              
            content.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

            content.setHeading(new Text("Ajouter un compte"));
            Text text = new Text("Est-ce que vous voulez vraiement supprimer votre compte monsieur "+text_nom.getText()+" ?");
            text.setFill(Color.RED);
  
            content.setBody(text);
            stackPane.setPrefHeight(150);
            stackPane.setPrefWidth(150);
            
          pane.getChildren().add(stackPane);
          stackPane.setLayoutX(175);
          stackPane.setLayoutY(150);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            JFXButton buttonOK = new JFXButton("OK");
            buttonOK.setOnAction(((event) -> {
                 try {
                     //supprimer un compte avec refresh de table
                     dao.deleteCompte(Integer.parseInt(text_num.getText()));
                 } catch (IllegalOrphanException ex) {
                     Logger.getLogger(GererComptesController.class.getName()).log(Level.SEVERE, null, ex);
                 } catch (NonexistentEntityException ex) {
                     Logger.getLogger(GererComptesController.class.getName()).log(Level.SEVERE, null, ex);
                 }
            comptes_list.clear();
            comptes_list.addAll(dao.getComptes());
            clearTexts();
            dialog.close();
            pane.getChildren().remove(stackPane);
            }));
            JFXButton buttonNO =new JFXButton("NO");
            buttonNO.setOnAction(((event) -> {
                dialog.close();
pane.getChildren().remove(stackPane);            }));
            content.setActions(buttonOK,buttonNO);

            dialog.show();
            
        }
    }
public void clearTexts(){
    text_num.setText("");
    text_nom.setText("");
    text_solde.setText("");
    text_prenom.setText("");
    
}

   
    public void filterComptes(){
    FilteredList<Compte> filteredData = new FilteredList<>(comptes_list, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        text_recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(compte -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (compte.getId().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } 
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Compte> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
}
}
