package afishaParsers.teatre;

class Spectacle {
    private String date;
    private String time;
    private String name;
    private String description;
    private String theatreName;
    private String cost;

    public Spectacle(String date, String time, String name, String description, String theatreName, String cost) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.description = description;
        this.theatreName = theatreName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return date + " - " + time + " - " + name + "\n" +  description + " " + theatreName + " - " + cost;
    }
}
