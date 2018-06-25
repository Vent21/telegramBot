package afishaParsers.movie;

class Movie{
    private String time;
    private String movieName;
    private String duration;
    private String description;
    private String cost;
    private String room;

    Movie(String time, String movieName, String description, String duration, String cost, String room) {
        this.time = time;
        this.movieName = movieName;
        this.duration = duration;
        this.description = description;
        this.cost = cost;
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return time + " - " + movieName + " " + " - " + cost + "\n";
        //return time + " " + movieName + " " + " " + description + " " + duration + " " + cost + " " + room;
    }
}
