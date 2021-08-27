package com.example.app5;

public class GsonClass {
    Movies[] d;
    class Movies{
        class ImgInfo {
            String imageUrl;
        }

        String l, q, s, y;
        ImgInfo i;

        public String getImage() {
            return i.imageUrl;
        }

        public String getName() {
            return l;
        }

        public String getType() { return q; }

        public String getActors() { return s; }

        public String getYear() {return y;}
    }

    public Movies[] getMovieList() {
        return d;
    }
}
