package com.example.finalproject;


/** shifts the array in various directions.
 */

public class Transform {
    /**
     * Blurs the image as much as possible.
     *
     * @param inputArray the given image
     * @param blurAmount how much (higher number is less blur)
     * @return the shifted image
     */
    public static RGBAPixel[][] blurring(final RGBAPixel[][] inputArray, final int blurAmount) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        int tileSizeX = inputArray.length / blurAmount;
        int tileSizeY = inputArray[0].length / blurAmount;
        int leftoversOne =  inputArray.length % blurAmount;
        int leftoversTwo = inputArray[0].length % blurAmount;
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        RGBAPixel[][] tile = new RGBAPixel[tileSizeX][tileSizeY];
        int counter = 0;
        big: for (int i = 0; i < inputArray.length - leftoversOne; i++) {
            for (int j = 0; j < inputArray[i].length - leftoversTwo; j++) {
                /*
                tile[i][j] = inputArray[i + counter][j + counter];
                */
                /*
                 * this is copying
                 */
                for (int k = 0; k < tileSizeX; k++) {
                    for (int l = 0; l < tileSizeY; l++) {
                        tile[k][l] = inputArray[i + k][j + l];
                    }
                }
                /*
                 * turning them into the blurred pixels.
                 */
                for (int k = 0; k < tileSizeX; k++) {
                    for (int l = 0; l < tileSizeY; l++) {
                        tile[i][j] = inputArray[i + counter][j + counter];
                        result[i + k][j + l] = RGBAPixel.blur(tile, tileSizeX, tileSizeY);
                    }
                }
                i += tileSizeX;
                j += tileSizeY;
                continue big;
            }
        }

        /*
         * Getting the bottom parts
         */
        int startingPoint = inputArray[0].length - leftoversTwo;
        RGBAPixel[][] BRemains = new RGBAPixel[tileSizeX][leftoversTwo];
        for (int i = 0; i < inputArray.length - leftoversOne; i++) {
            /*
             * copying
             */
            for (int k = 0; k < tileSizeX; k++) {
                for (int l = 0; l < leftoversTwo; l++) {
                    BRemains[k][l] = inputArray[i + k][startingPoint + l];
                }
            }
            /*
             *blurring
             */
            for (int k = 0; k < tileSizeX; k++) {
                for (int l = 0; l < leftoversTwo; l++) {
                    result[i + k][startingPoint + l]
                            = RGBAPixel.blur(BRemains, tileSizeX, leftoversTwo);
                }
            }
            i += tileSizeX;
        }
        /*
        blurring the side part
         */
        int startingPointS = inputArray.length - leftoversOne;
        RGBAPixel[][] SRemains = new RGBAPixel[leftoversOne][tileSizeY];
        for (int i = 0; i < inputArray.length - leftoversOne; i++) {
            /*
             * copying
             */
            for (int k = 0; k < leftoversOne; k++) {
                for (int l = 0; l < tileSizeY; l++) {
                    SRemains[k][l] = inputArray[startingPointS + k][i + l];
                }
            }
            /*
             *blurring
             */
            for (int k = 0; k < leftoversOne; k++) {
                for (int l = 0; l < tileSizeY; l++) {
                    result[startingPointS + k][i + l]
                            = RGBAPixel.blur(SRemains, leftoversOne, tileSizeY);
                }
            }
            i += tileSizeY;
        }
        RGBAPixel[][] lastbit = new RGBAPixel[leftoversOne][leftoversTwo];
        for (int i = 0; i < leftoversOne; i++) {
            for (int j = 0; j < leftoversTwo; j++) {
                lastbit[i][j] = inputArray[startingPointS + i][startingPoint + j];
            }
        }
        for (int i = 0; i < leftoversOne; i++) {
            for (int j = 0; j < leftoversTwo; j++) {
                result[startingPointS + i][startingPoint + j]
                        = RGBAPixel.blur(lastbit, leftoversOne, leftoversTwo);
            }
        }
        return result;
    }

}

