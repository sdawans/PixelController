/**
 * Copyright (C) 2014 Sébastien Dawans <sebastien@dawans.be>
 *
 * This file is part of PixelController.
 *
 * PixelController is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PixelController is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PixelController.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.neophob.sematrix.core.visual.generator;

import com.neophob.sematrix.core.resize.Resize.ResizeName;
import com.neophob.sematrix.core.visual.MatrixData;

/**
 * The GameOfLife Class.
 */
public class GameOfLife extends Generator {

    /** The last and next iteration */
    private boolean[][] last, next;

    private int game_x = 8;
    private int game_y = 8;

    private int color;

    /**
     * Instantiates a new GameOfLife.
     * 
     * @param controller
     *            the controller
     */
    public GameOfLife(MatrixData matrix) {
        super(matrix, GeneratorName.GAME_OF_LIFE, ResizeName.QUALITY_RESIZE);
        last = new boolean[game_x][game_y];
        next = new boolean[game_x][game_y];
        color = 0;
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.neophob.sematrix.core.generator.Generator#update()
     */
    @Override
    public void update(int amount) {

        /* Speed of the generator dictates the number of iterations of Life */
        for (int i=0; i<amount; i++) {
            step();
        }

        // draw
        int xp,yp;
        for (int y = 0; y < internalBufferYSize; y++) {
            for (int x = 0; x < internalBufferXSize; x++) {
                xp = game_x < internalBufferXSize ? x * game_x / internalBufferXSize : x;
                yp = game_y < internalBufferYSize ? y * game_y / internalBufferYSize : y;
                this.internalBuffer[y * internalBufferXSize + x] = next[xp][yp] ? 180 : 0;
            }
        }
    }

    /* Initial state generation */
    private void init() {
        for (int y = 0; y < game_y; y++) {
            for (int x = 0; x < game_x; x++) {
                last[x][y] = false;
            }
        }
        /* Glider */
        last[3][2] = true;
        last[4][3] = true;
        last[2][4] = true;
        last[3][4] = true;
        last[4][4] = true;
    }

    private void step() {
        for (int y = 0; y < game_y; y++) {
            for (int x = 0; x < game_x; x++) {
                next[x][y] = willLive(last[x][y], getAliveNeighbors(x,y));
            }
        }
        for (int y = 0; y < game_y; y++) {
            for (int x = 0; x < game_x; x++) {
                last[x][y] = next[x][y];
            }
        }
    }

    private int getAliveNeighbors(int xpos, int ypos) {
        int count = 0, xx, yy;
        for (int y=ypos-1; y<=ypos+1; y++){
            for (int x=xpos-1; x<=xpos+1; x++){
                xx = (x % game_x + game_x) % game_x;
                yy = (y % game_y + game_y) % game_y;

                if (last[xx][yy]) {
                    count++;
                }
            }
        }
        if (last[xpos][ypos]) {
            count -= 1;
        }
        return count;
    }

    private boolean willLive(boolean alive, int aliveNeighbors) {
        if (alive && (aliveNeighbors == 3 || aliveNeighbors == 2)) {
            return true;
        }
        else if (!alive && aliveNeighbors == 3) {
            return true;
        }
        else {
            return false;
        }

    }

}
