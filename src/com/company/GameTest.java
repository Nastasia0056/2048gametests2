package com.company;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();

    }

    @Test
    public void moveTest1() {
        int[][] expected = {{0,0,0,0},{4,0,0,0},{0,0,0,0},{2,0,0,0}};

        game.set(1, 1, 4);
        game.set(3, 3, 2);

        game.move(DirectionType.Left);

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], game.get(i, j));
            }
        }
    }

    @Test
    public void moveTestMerge() {
        int[][] expected = {{0,0,0,0},{0,0,0,4},{0,0,0,0},{0,0,0,4}};
        int expectedScore = 4;

        game.set(1, 0, 4);
        game.set(3, 0, 2);
        game.set(3, 2, 2);

        game.move(DirectionType.Right);

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], game.get(i, j));
            }
        }
        assertEquals(expectedScore, game.getScore());
    }

    @Test
    public void moveTestLoose() {
        StatusType expectedStatus = StatusType.Loose;

        for (int i = 0; i < Game.SIZE; i++) {
            for (int j = 0; j < Game.SIZE; j++) {
                game.set(i,j,i * Game.SIZE + j + 1);
            }
        }

        game.move(DirectionType.Right);

        assertEquals(expectedStatus, game.getStatus());
    }

    @Test
    public void moveTestWin() {
        int[][] expected = {{0,0,0,0},{0,0,0,0},{4,0,0,0},{2,2048,2,0}};
        StatusType expectedStatus = StatusType.Win;

        game.set(0, 0, 4);
        game.set(1, 0, 2);
        game.set(0, 1, 1024);
        game.set(1, 1, 1024);
        game.set(0, 2, 2);

        game.move(DirectionType.Down);

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], game.get(i, j));
            }
        }
        assertEquals(expectedStatus, game.getStatus());
    }
}