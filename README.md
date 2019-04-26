# SongLibrary

Song library GUI built using JavaFX, Eclipse, and Scenebuilder in Java 8.

## Features

+ Display a list of songs by name, with the ability to select one song from the list.
    + List data is stored as a file, to persist across multiple sessions.
+ Display the details of the selected song: name, artist, year, album.
+ Add, Edit, and Delete songs
    + Adding songs requires at least the name and artist, and duplicates are not allowed.
    + Editing songs allows for changing any detail of a song, but cannot have duplicates again.
    + The user is able to delete any song.
    + Every action requests confirmation from the user before making the final changes.

### Song list implementation

The song list is implemented with a `ListView`, a parameterized JavaFX class that allows for the user to select an item from the list. In order to have song details update on every click, the `ListView` is wrapped in an `ObservableList`, which allows for setting an event handler on the selection change using Java 8's lambda expressions.

```java
//SongController.java
showSongDetails();
    
//Set listener for selection changes
songList.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldValue, newValue) -> showSongDetails());

...

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
```

### `Song` Class

The `Song` class implements the `Comparable` interface to allow for sorting the song list alphabetically, as well as finding the index position of a song by overriding the `compareTo` method.

```java

//Song.java

@Override
public int compareTo(Song s) {
    if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) == 0){
        if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) == 0){
            return 0;
        }
        else if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) > 0){
            return 1;
        }
        else{
            return -1;
        }
    }
    else if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) > 0){
        return 1;
    }
    else{
        return -1;
    }
}
```

### Saving files

List persistence was implemented by creating a `songs.txt` file in the data directory to read and write from. We chose to use a `PrintWriter` to write to the file, and add each detail as a separate line to read from. By setting a simple counter, we break the file into chunks of 4 to load as songs into the `ObservableList`. Using the `FXCollections.sort()` function allows for alphabetical sorting regardless of when a song is inputted.

#### Reading from file: 
```java
//SongController.java
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
```

#### Writing to file

When the program is closed, it will automatically write to file what songs you have in your list.

```java
//SongController.java

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

```
