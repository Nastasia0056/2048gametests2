package com.company;

import java.util.Random;

public class Game {
    public static final int SIZE = 4;
    public static final int WIN_SCORE = 2048;
    private int[][] matrix = new int[SIZE][SIZE];
    private int score = 0;
    private Random random = new Random();
    private StatusType status;

    public Game(){
        newGame();
    }

    public int get(int i, int j){
        return matrix[i][j];
    }

    public int getScore() {
        return score;
    }

    public void newGame(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = 0;
            }
        }

        status = StatusType.Play;
        score = 0;
        createNumber();
        createNumber();
    }

    private void createNumber(){
        int ver = random.nextInt(11);
        int data;
        if(ver == 10)
            data = 4;
        else
            data = 2;

        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        }
        while (matrix[row][col] != 0);

        matrix[row][col] = data;
    }

    //проверка на то что матрица полностью заполнена
    private boolean checkFull(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(matrix[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    //слияние ячеек если значения совпадают
    private boolean mergeCells(int row, int col, int data){
        if (matrix[row][col] == data){ //если их значения равны то склеиваем ячейки (сумма)
            matrix[row][col] += data;
            if (matrix[row][col] == WIN_SCORE){ //если набрали 2048
                status = StatusType.Win;
            }
            score += matrix[row][col]; //увеличиваем счет
            return true; //выход из функции
        }
        return false;
    }

    private void moveUp(int row, int col){
        //если перемещаемая ячейка = 0 то ничего не надо делать
        if(matrix[row][col] == 0)
            return;
        int data = matrix[row][col]; //запоминаем данные ячейки
        matrix[row][col] = 0; //удаляем ячейку
        int endRow = row; //конечная строка до куда поднимутся данные
        
        for (int i = row - 1; i >= 0; i--) {
            if(matrix[i][col] != 0){ //если над эл-тов встретили не ноль
                if (mergeCells(i, col, data))
                    return;
                break;
            }
            endRow--;
        }
        matrix[endRow][col] = data;
    }

    private void moveDown(int row, int col){
        if(matrix[row][col] == 0)
            return;
        int data = matrix[row][col]; //запоминаем данные ячейки
        matrix[row][col] = 0; //удаляем ячейку
        int endRow = row; //конечная строка до куда поднимутся данные

        for (int i = row + 1; i < SIZE; i++) {
            if(matrix[i][col] != 0){
                if (mergeCells(i, col, data))
                    return;
                break;
            }
            endRow++;
        }
        matrix[endRow][col] = data;
    }

    private void moveLeft(int row, int col){
        if(matrix[row][col] == 0)
            return;
        int data = matrix[row][col]; //запоминаем данные ячейки
        matrix[row][col] = 0; //удаляем ячейку
        int endCol = col;

        for (int j = col - 1; j >= 0; j--) {
            if(matrix[row][j] != 0){
                if (mergeCells(row, j, data))
                    return;
                break;
            }
            endCol--;
        }
        matrix[row][endCol] = data;
    }

    private void moveRight(int row, int col){
        if(matrix[row][col] == 0)
            return;
        int data = matrix[row][col]; //запоминаем данные ячейки
        matrix[row][col] = 0; //удаляем ячейку
        int endCol = col;

        for (int j = col + 1; j < SIZE; j++) {
            if(matrix[row][j] != 0){
                if (mergeCells(row, j, data))
                    return;
                break;
            }
            endCol++;
        }
        matrix[row][endCol] = data;
    }

    private boolean checkMoveLeft(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 1; j < SIZE; j++) {
                if (matrix[i][j-1] == 0 || matrix[i][j-1] == matrix[i][j])
                    return true;
            }
        }
        return false;
    }

    private boolean checkMoveRight(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = SIZE - 2; j >= 0; j--) {
                if (matrix[i][j+1] == 0 || matrix[i][j+1] == matrix[i][j])
                    return true;
            }
        }
        return false;
    }

    private boolean checkMoveUp(){
        for (int i = 1; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i-1][j] == 0 || matrix[i-1][j] == matrix[i][j])
                    return true;
            }
        }
        return false;
    }

    private boolean checkMoveDown(){
        for (int i = SIZE - 2; i >= 0; i--) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i+1][j] == 0 || matrix[i+1][j] == matrix[i][j])
                    return true;
            }
        }
        return false;
    }

    private boolean checkMove(){
        if (checkMoveDown() || checkMoveLeft() || checkMoveRight() || checkMoveUp())
            return true;
        return false;
    }

    public void move(DirectionType directionType){
        if (!checkMove())
            status = StatusType.Loose;

        if (status != StatusType.Play)
            return;

        if (directionType == DirectionType.Left && !checkMoveLeft())
            return;
        if (directionType == DirectionType.Right && !checkMoveRight())
            return;
        if (directionType == DirectionType.Up && !checkMoveUp())
            return;
        if (directionType == DirectionType.Down && !checkMoveDown())
            return;

        if(directionType == DirectionType.Up){
            for (int j = 0; j < SIZE; j++) {
                for (int i = 1; i < SIZE; i++) {
                    moveUp(i, j);
                }
            }
        }
        else if (directionType == DirectionType.Down){
            for (int j = 0; j < SIZE; j++) {
                for (int i = SIZE - 2; i >= 0; i--) {
                    moveDown(i, j);
                }
            }
        }
        else if (directionType == DirectionType.Left){
            for (int i = 0; i < SIZE; i++) {
                for (int j = 1; j < SIZE; j++) {
                    moveLeft(i, j);
                }
            }
        }
        else {
            for (int i = 0; i < SIZE; i++) {
                for (int j = SIZE - 2; j >= 0; j--) {
                    moveRight(i, j);
                }
            }
        }

        if(!checkFull())
            createNumber();
    }

//    public void print(){
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                System.out.print(matrix[i][j] + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }

    public StatusType getStatus() {
        return status;
    }
}
