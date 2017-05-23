package com.company;
import java.awt.Point;
import java.util.Iterator;
import java.util.TreeSet;
/**
 * Created by bush on 27.02.2017.
 */
public class DelaunayTriangulation
{
    int[][] adjMatrix;

    DelaunayTriangulation(int size)
    {
        this.adjMatrix = new int[size][size];
    }
    public int[][] getAdj() {
        return this.adjMatrix;
    }

    public TreeSet getEdges(int n, int[] x, int[] y, int[] z)
    {
        TreeSet result = new TreeSet();

        if (n == 2)
        {
            this.adjMatrix[0][1] = 1;
            this.adjMatrix[1][0] = 1;
            result.add(new GraphEdge(new GraphPoint(x[0], y[0]), new GraphPoint(x[1], y[1])));

            return result;
        }

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = i + 1; k < n; k++)
                {
                    if (j == k) {
                        continue;
                    }
                    int xn = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i]) * (z[j] - z[i]);

                    int yn = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i]) * (z[k] - z[i]);

                    int zn = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i]) * (y[j] - y[i]);
                    boolean flag;
                    if (flag = (zn < 0 ? 1 : 0) != 0) {
                        for (int m = 0; m < n; m++) {
                            flag = (flag) && ((x[m] - x[i]) * xn + (y[m] - y[i]) * yn + (z[m] - z[i]) * zn <= 0);
                        }

                    }

                    if (!flag)
                    {
                        continue;
                    }
                    result.add(new GraphEdge(new GraphPoint(x[i], y[i]), new GraphPoint(x[j], y[j])));
                    //System.out.println("----------");
                    //System.out.println(x[i]+" "+ y[i] +"----"+x[j]+" "+y[j]);

                    result.add(new GraphEdge(new GraphPoint(x[j], y[j]), new GraphPoint(x[k], y[k])));
                    //System.out.println(x[j]+" "+ y[j] +"----"+x[k]+" "+y[k]);
                    result.add(new GraphEdge(new GraphPoint(x[k], y[k]), new GraphPoint(x[i], y[i])));
                    //System.out.println(x[k]+" "+ y[k] +"----"+x[i]+" "+y[i]);
                    this.adjMatrix[i][j] = 1;
                    this.adjMatrix[j][i] = 1;
                    this.adjMatrix[k][i] = 1;
                    this.adjMatrix[i][k] = 1;
                    this.adjMatrix[j][k] = 1;
                    this.adjMatrix[k][j] = 1;
                }

            }

        }

        return result;
    }

    public TreeSet getEdges(TreeSet pointsSet)
    {
        if ((pointsSet != null) && (pointsSet.size() > 0))
        {
            int n = pointsSet.size();

            int[] x = new int[n];
            int[] y = new int[n];
            int[] z = new int[n];

            int i = 0;

            Iterator iterator = pointsSet.iterator();
            while (iterator.hasNext())
            {
                Point point = (Point)iterator.next();

                x[i] = (int)point.getX();
                y[i] = (int)point.getY();
                z[i] = (x[i] * x[i] + y[i] * y[i]);

                i++;
            }

            return getEdges(n, x, y, z);
        }

        return null;
    }
}