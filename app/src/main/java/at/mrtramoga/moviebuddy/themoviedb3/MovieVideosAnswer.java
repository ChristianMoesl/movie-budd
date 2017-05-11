package at.mrtramoga.moviebuddy.themoviedb3;

import java.util.List;

/**
 * Created by chris_000 on 25.07.2016.
 */
public class MovieVideosAnswer {

    public static class Video {

        private String id;

        private String iso6391;

        private String key;

        private String name;

        private String site;

        private long size;

        private String type;

        public String getId() {
            return id;
        }

        public String getIso6391() {
            return iso6391;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public long getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

    }

    private long id;

    private List<Video> results;

    public List<Video> getVideos() {
        return results;
    }

    public long getId() {
        return id;
    }
}
