/**
 * Song library
 * Andrew Wang
 * Rumzi Tadros
 */
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Song;

public class SongController {
	
	@FXML
	private ListView<Song> songList;
	@FXML
	private TextField titleAddField;
	@FXML
	private TextField titleEditField;
	@FXML
	private TextField artistAddField;
	@FXML
	private TextField artistEditField;
	@FXML
	private TextField albumAddField;
	@FXML
	private TextField albumEditField;
	@FXML
	private TextField yearAddField;
	@FXML
	private TextField yearEditField;
	
	
	private ObservableList<Song> obsList;
	
	
	public void start(Stage primaryStage) {
		
		File data = new File("src/data/songs.txt");
		obsList = FXCollections.observableArrayList();
		if(data.exists() && !data.isDirectory()){
			try {
				Scanner fileIn = new Scanner(data);
				
				int lines = 0;
				while (fileIn.hasNextLine()) {
				    lines++;
				    fileIn.nextLine();
				}
				fileIn.close();
				fileIn = new Scanner(data);
				lines -= 2;
				fileIn.nextLine();
				fileIn.nextLine();
				if(lines%4 == 0){
					for(int i=0; i<lines; i+=4){
						obsList.add(new Song(fileIn.nextLine(), fileIn.nextLine(), fileIn.nextLine(), fileIn.nextLine()));
					}
					/*
					 * The Following should work
					 * using the compareTo method in song
					 * https://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html
					 */
					FXCollections.sort(obsList);
				}
				else{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("File Error");
					alert.setContentText("Formatting of file is not modulus 4");
					alert.showAndWait();
				}
				
				fileIn.close();
				
			} catch (FileNotFoundException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING");
				alert.setHeaderText("File Error");
				alert.setContentText("File does not exist");
				alert.showAndWait();
				e.printStackTrace();
			}
			
		}
		songList.setItems(obsList);
		if(!obsList.isEmpty()) {
			songList.getSelectionModel().selectFirst();
		}
		
		showSongDetails();
		
		//Set listener for selection changes
		songList.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldValue, newValue) -> showSongDetails());
			
		//Write to file when program is closed
		primaryStage.setOnCloseRequest(event -> {
			PrintWriter write;
			try {
				File file = new File("src/data/songs.txt");
				file.createNewFile();
				write = new PrintWriter(file);
				write.println("Messing with song file format will result");
				write.println("IN LOSING ALL YOUR SONGS");
				for(int i = 0; i < obsList.size(); i++) {
					write.println(obsList.get(i).getTitle());
					write.println(obsList.get(i).getArtist());
					write.println(obsList.get(i).getAlbum());
					write.print(obsList.get(i).getYear());
					if(i != obsList.size()-1){
						write.println("");
					}
				}
				write.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		titleAddField.setText("");
		artistAddField.setText("");
		albumAddField.setText("");
		yearAddField.setText("");
	}
	
	
	/**
	 * Handles delete button
	 */
	@FXML
	private void handleDelete() {
		if(obsList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Nothing to delete");
			alert.setContentText("Song list is empty - nothing to delete.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("WARNING");
			alert.setHeaderText("You are about to delete the selected song");
			alert.setContentText("Are you sure?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				int selectedIndex = songList.getSelectionModel().getSelectedIndex();
				obsList.remove(selectedIndex);
				
				if(obsList.isEmpty()) {			//Removed the only item in the list
					titleEditField.setText("");
					artistEditField.setText("");
					albumEditField.setText("");
					yearEditField.setText("");
				} else if (selectedIndex == obsList.size()-1) {
					songList.getSelectionModel().select(selectedIndex--);
				} else {
					songList.getSelectionModel().select(selectedIndex++);
				}
			} else {
				return;
			}
		}
	}
	
	/**
	 * Handles add button
	 */
	@FXML
	private void handleAdd() {
		if(albumAddField.getText().compareTo("") == 0) {
			albumAddField.setText(" ");
		}
		if(yearAddField.getText().compareTo("") == 0) {
			yearAddField.setText(" ");
		}
		Song tempSong = new Song(titleAddField.getText(), artistAddField.getText(),
								albumAddField.getText(), yearAddField.getText());
		add(tempSong);
	}
	
	private int add(Song tempSong){
		int index = findIndex(obsList,tempSong);
		String artist, title;
			artist = tempSong.getArtist();
			title = tempSong.getTitle();
		if(artist.compareTo("") == 0 || title.compareTo("") == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Missing input");
			alert.setContentText("Title and Artist input must not be empty");
			alert.showAndWait();
			return -1;
		} else if (index == -1) {			//Scan through and check for duplicate
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING");
			alert.setHeaderText("Same title/artist combination already exists in file");
			alert.showAndWait();
			return -1;
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("WARNING");
			alert.setHeaderText("Adding a new song");
			alert.setContentText("You are about to add a new song: \n" + title + " by: " 
									+ artist + "\nAre you sure?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				obsList.add(index, tempSong);
				titleAddField.setText("");
				artistAddField.setText("");
				albumAddField.setText("");
				yearAddField.setText("");
				return 0;
			} else {
				showSongDetails();
			}
		}
		return 0;
	}
	
	/**
	 * Handles edit button
	 */
	@FXML
	private void handleEdit() {
		
		Song selectedSong = songList.getSelectionModel().getSelectedItem();
		Song tempSong = new Song(titleEditField.getText(), artistEditField.getText(),
				albumEditField.getText(), yearEditField.getText());
		
		if(selectedSong.getTitle().compareTo(tempSong.getTitle()) == 0 && 
		   selectedSong.getArtist().compareTo(tempSong.getArtist()) == 0) {
			songList.getSelectionModel().getSelectedItem().setAlbum(tempSong.getAlbum());
			songList.getSelectionModel().getSelectedItem().setYear(tempSong.getYear());
		} else { 
			obsList.remove(songList.getSelectionModel().getSelectedIndex());
			if(add(tempSong)== -1){
				add(selectedSong);
			}
			
		}
	}
	
	private void showSongDetails() {
		if(songList.getSelectionModel().getSelectedIndex() < 0) {
			return;
		}
		
		Song song = songList.getSelectionModel().getSelectedItem();
		titleEditField.setText(song.getTitle());
		artistEditField.setText(song.getArtist());
		albumEditField.setText(song.getAlbum());
		yearEditField.setText(song.getYear());
	}
	public static int findIndex(ObservableList<Song> list, Song s){
		int i;
		for(i = 0; i < list.size(); i++){
			if(list.get(i).compareTo(s)==0){
				return -1; //implies an error, song already found
			}
			else if(list.get(i).compareTo(s)>0){
				return i; //take position in list
			}
			else{
				// (list.get(i).compareTo(s)<0)
				continue; //keep iterating for a spot
			}
		}
		// (i == list.size()-1)
		return i;//Append to end of list
	}
	
}
