package com.freelancer.tvprograme2.dto;

import java.util.List;

/**
 * Data class representing a tv programme object.
 */
public class TvProgramme {
    List<ProgrammDataObject> results;
    int count;

    public List<ProgrammDataObject> getResults() {
        return results;
    }

    public void setResults(List<ProgrammDataObject> results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public class ProgrammDataObject{
        String name;
        String start_time;
        String end_time;
        String channel;
        String rating;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }

}
