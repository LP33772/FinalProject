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
        int tileSizeY = inputArray[0].length / blurAmount
        int leftoversOne =  inputArray.length % blurAmount;
        int leftoversTwo = inputArray[0].length % blurAmount;
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        RGBAPixel[][] tile = new RGBAPixel[tileSizeX][tileSizeY];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                for (int k = 0; k < tileSizeX; k++) {
                    result[i][j] = RGBAPixel.blur(inputArray, tileSize);
                }
            }
        }
        return result;
    }
    /**
     * shifts the array right, I believe.
     * @param inputArray the given image
     * @param shiftRight how right the image shifts
     * @return the shifted image
     */

    public static RGBAPixel[][] shiftRight(final RGBAPixel[][] inputArray, final int shiftRight) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = shiftRight; i < inputArray.length; i++) {
            for (int j = inputArray[i].length - 1; j >= 0; j--) {
                result[i][j] = inputArray[i - shiftRight][j];
            }
        }
        return result;
    }

    /**
     * shifts the array up.
     * @param inputArray the given image
     * @param shiftUp how much to shift
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftUp(final RGBAPixel[][] inputArray, final int shiftUp) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length - shiftUp; j++) {
                result[i][j] = inputArray[i][j + shiftUp];
            }
        }
        return result;
    }

    /**
     * shifts the array Down.
     * @param inputArray the given image
     * @param shiftDown how much to shift
     * @return the shifted image
     */
    public static RGBAPixel[][] shiftDown(final RGBAPixel[][] inputArray, final int shiftDown) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = inputArray[i].length - 1; j >= shiftDown; j--) {
                result[i][j] = inputArray[i][j - shiftDown];
            }
        }
        return result;
    }


    /**
     * Rotates the array counterclockwise.
     * @param inputArray the given image
     * @return the rotated image
     */
    public static RGBAPixel[][] rotateLeft(final RGBAPixel[][] inputArray) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }

        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        int smallerDimension;

        if (inputArray.length > inputArray[0].length) {
            smallerDimension = inputArray[0].length;
        } else {
            smallerDimension = inputArray.length;
        }

        int spacingX = (result.length - smallerDimension) / 2;
        int spacingY = (result[0].length - smallerDimension) / 2;

        RGBAPixel[][] squared = new RGBAPixel[smallerDimension][smallerDimension];
        for (int i = 0; i < smallerDimension; i++) {
            for (int j = 0; j < smallerDimension; j++) {
                squared[i][j] = inputArray[i + spacingX][j + spacingY];
            }
        }


        for (int i = 0; i < squared.length; i++) {
            for (int j = 0; j < squared.length; j++) {
                result[i + spacingX][j + spacingY] = squared[squared.length - 1 - j][i];
            }
        }
        return result;
    }

    /**
     * Rotates the array clockwise.
     * @param inputArray the given image
     * @return the rotated image
     */

    public static RGBAPixel[][] rotateRight(final RGBAPixel[][] inputArray) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }

        int smallerDimension;

        if (inputArray.length > inputArray[0].length) {
            smallerDimension = inputArray[0].length;
        } else {
            smallerDimension = inputArray.length;
        }

        int spacingX = (result.length - smallerDimension) / 2;
        int spacingY = (result[0].length - smallerDimension) / 2;

        RGBAPixel[][] squared = new RGBAPixel[smallerDimension][smallerDimension];

        for (int i = 0; i < smallerDimension; i++) {
            for (int j = 0; j < smallerDimension; j++) {
                squared[i][j] = inputArray[i + spacingX][j + spacingY];
            }
        }

        for (int i = 0; i < squared.length; i++) {
            for (int j = 0; j < squared.length; j++) {
                result[i + spacingX][j + spacingY] = squared[j][squared.length - 1 - i];
            }
        }
        return result;
    }

    /**
     * Flips vertically.
     * @param inputArray the given image
     * @return the flipped image
     */

    public static RGBAPixel[][] flipVertical(final RGBAPixel[][] inputArray) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = 0; i < (inputArray.length); i++) {
            for (int j = 0; j < inputArray[0].length; j++) {
                result[i][j] = inputArray[i][inputArray[0].length - 1 - j];
            }
        }
        return result;
    }
    /**
     * Flips Horizontally.
     * @param inputArray the given image
     * @return the flipped image
     */

    public static RGBAPixel[][] flipHorizontal(final RGBAPixel[][] inputArray) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }

        for (int i = 0; i < (inputArray[0].length); i++) {
            for (int j = 0; j < inputArray.length; j++) {
                result[j][i] = inputArray[inputArray.length - 1 - j][i];
            }
        }
        return result;
    }

    /**
     * Expands the image vertically.
     * @param inputArray the given image
     * @param expandVertical how much to expand
     * @return the expanded image
     */

    public static RGBAPixel[][] expandVertical(final RGBAPixel[][] inputArray, final int expandVertical) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }

        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[0].length / 2; j++) {
                result[i][j + (inputArray[0].length / 2)]
                        = inputArray[i][(inputArray[0].length / 2) + ((j) / (expandVertical))];
                result[i][(inputArray[0].length / 2) - 1 - j]
                        = inputArray[i][((inputArray[0].length / 2) - 1) - ((j) / (expandVertical))];
            }
        }
        return result;
    }

    /**
     * Expands the image Horizontally.
     * @param inputArray the given image
     * @param expandHorizontal how much to expand
     * @return the expanded image
     */

    public static RGBAPixel[][] expandHorizontal(final RGBAPixel[][] inputArray, final int expandHorizontal) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }

        for (int i = 0; i < inputArray[0].length; i++) {
            for (int j = 0; j < inputArray.length / 2; j++) {
                result[j + (inputArray.length / 2)][i]
                        = inputArray[(inputArray.length / 2) + ((j) / (expandHorizontal))][i];
                result[(inputArray.length / 2) - 1 - j][i]
                        = inputArray[((inputArray.length / 2) - 1) - ((j) / (expandHorizontal))][i];
            }
        }

        return result;
    }

    /**
     * shrinks the image vertically.
     * @param inputArray the given image
     * @param shrinkVertical how much to expand
     * @return the expanded image
     */

    public static RGBAPixel[][] shrinkVertical(final RGBAPixel[][] inputArray, final int shrinkVertical) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[0].length / 2 - shrinkVertical; j++) {
                result[i][j + (inputArray[0].length / 2)]
                        = inputArray[i][(inputArray[0].length / 2) + ((j) * (shrinkVertical))];
                result[i][(inputArray[0].length / 2) - 1 - j]
                        = inputArray[i][((inputArray[0].length / 2) - 1) - ((j) * (shrinkVertical))];
            }
        }
        return result;
    }

    /**
     * shrinks the image Horizontally.
     * @param inputArray the given image
     * @param shrinkHorizontal how much to expand
     * @return the expanded image
     */

    public static RGBAPixel[][] shrinkHorizontal(final RGBAPixel[][] inputArray, final int shrinkHorizontal) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }

        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        for (int i = 0; i < inputArray[0].length; i++) {
            for (int j = 0; j < (inputArray.length / 2 - shrinkHorizontal); j++) {
                result[j + (inputArray.length / 2)][i]
                        = inputArray[(inputArray.length / 2) + ((j) * (shrinkHorizontal))][i];
                result[(inputArray.length / 2) - 1 - j][i]
                        = inputArray[((inputArray.length / 2) - 1) - ((j) * (shrinkHorizontal))][i];
            }
        }
        return result;
    }

    /**
     * Converts green pixels to blanks.
     * @param inputArray the given image
     * @return the image with transparency
     */

    public static RGBAPixel[][] greenScreen(final RGBAPixel[][] inputArray) {
        if (inputArray == null || inputArray.length == 0 || inputArray[0].length == 0) {
            return null;
        }
        RGBAPixel[][] result = new RGBAPixel[inputArray.length][inputArray[0].length];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                result[i][j] = RGBAPixel.getFillValue();
            }
        }
        int[][] checking = RGBAPixel.toIntArray(inputArray);

        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                if (checking[i][j] == GREEN) {
                    continue;
                } else {
                    result[i][j] = inputArray[i][j];
                }
            }
        }
        return result;
    }


}

