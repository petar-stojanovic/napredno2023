package src.labs.lab8;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Song {

    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}", title, artist);
    }
}

interface State {
    void pressPlay();

    void pressStop();

    void pressFWD();

    void pressREW();

    void forward();
    void reward();
}

abstract class AbstractState implements State {
    MP3Player mp3Player;

    public AbstractState(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }

}

class PlayState extends AbstractState {

    public PlayState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.println("Song " + mp3Player.getCurrentSongIndex() + " is paused");
        mp3Player.setState(mp3Player.pause);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");

//        mp3Player.setCurrentSong(mp3Player.getCurrentSongIndex() + 1);
        mp3Player.setState(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setState(mp3Player.rew);
    }

    @Override
    public void forward() {

    }

    @Override
    public void reward() {

    }

}

class PauseState extends AbstractState {

    public PauseState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + mp3Player.getCurrentSongIndex() + " is playing");
        mp3Player.setState(mp3Player.play);
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are stopped");
        mp3Player.setCurrentSong(0);
        mp3Player.setState(mp3Player.stop);
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3Player.setState(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setState(mp3Player.rew);
    }

    @Override
    public void forward() {

    }

    @Override
    public void reward() {

    }

}

class StopState extends AbstractState {

    public StopState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {
        System.out.println("Song " + mp3Player.getCurrentSongIndex() + " is playing");
        mp3Player.setState(mp3Player.play);
    }

    @Override
    public void pressStop() {
        System.out.println("Songs are already stopped");
    }

    @Override
    public void pressFWD() {
        System.out.println("Forward...");
        mp3Player.setState(mp3Player.fwd);
    }

    @Override
    public void pressREW() {
        System.out.println("Reward...");
        mp3Player.setState(mp3Player.rew);
    }

    @Override
    public void forward() {

    }

    @Override
    public void reward() {

    }
}

class FWDState extends AbstractState {

    public FWDState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {

    }

    @Override
    public void pressStop() {

    }

    @Override
    public void pressFWD() {
    }

    @Override
    public void pressREW() {

    }

    @Override
    public void forward() {
        mp3Player.songFWD();
        mp3Player.setState(mp3Player.pause);
    }

    @Override
    public void reward() {

    }

}

class REWState extends AbstractState {

    public REWState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void pressPlay() {

    }

    @Override
    public void pressStop() {

    }

    @Override
    public void pressFWD() {

    }

    @Override
    public void pressREW() {
        mp3Player.setState(mp3Player.pause);
    }

    @Override
    public void forward() {

    }

    @Override
    public void reward() {
        mp3Player.songREW();
        mp3Player.setState(mp3Player.pause);
    }

}

class MP3Player {

    List<Song> songs;
    int currentSong;

    State state;
    State play;
    State pause;
    State stop;
    State fwd;
    State rew;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        currentSong = 0;
        createStates();
    }

    void createStates() {
        play = new PlayState(this);
        pause = new PauseState(this);
        stop = new StopState(this);
        fwd = new FWDState(this);
        rew = new REWState(this);

        state = stop;
    }


    public Song getCurrentSong() {
        return songs.get(currentSong);
    }

    public int getCurrentSongIndex() {
        return currentSong;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = (currentSong) % songs.size();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void songFWD(){
        currentSong = (currentSong + 1) % songs.size();
    }

    public void songREW(){
        currentSong = (currentSong + songs.size() - 1) % songs.size();
    }

    public void pressPlay() {
        state.pressPlay();
    }

    public void pressStop() {
        state.pressStop();
    }

    public void pressFWD() {
        state.pressFWD();
        state.forward();
//        songFWD();
    }

    public void pressREW() {
        state.pressREW();
        state.reward();
//        songREW();
    }

    public void printCurrentSong() {
        System.out.println(getCurrentSong());
    }

    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = %s}", currentSong, songs);
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);

        System.out.println(player.toString());
        System.out.println("First test");

        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();

        System.out.println(player.toString());
        System.out.println("Second test");

        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();

        System.out.println(player.toString());
        System.out.println("Third test");

        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();

        System.out.println(player.toString());
    }
}

//Vasiot kod ovde