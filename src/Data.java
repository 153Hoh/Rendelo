class Data {

    private String cikkcsop;
    private String cikkszam;
    private String cikknev;
    private double minrend;
    private String me;
    private double afa;
    private double masodar;
    private int index;

    Data(String cc, String cs, String cn, double min, String me, double a, double ma){
        this.cikkcsop = cc;
        this.cikkszam = cs;
        this.cikknev = cn;
        this.minrend = min;
        this.me = me;
        this.afa = a;
        this.masodar = ma;
    }

    Data(String cc, String cs, String cn, double min, String me, double a, double ma, int index){
        this.cikkcsop = cc;
        this.cikkszam = cs;
        this.cikknev = cn;
        this.minrend = min;
        this.me = me;
        this.afa = a;
        this.masodar = ma;
        this.index = index;
    }

    String getCC(){
        return cikkcsop;
    }

    String getCS(){
        return cikkszam;
    }

    String getCN(){
        return cikknev;
    }

    double getMin(){
        return minrend;
    }

    String getME(){
        return me;
    }

    double getAfa(){
        return afa;
    }

    double getMA(){
        return masodar;
    }

    int getIndex(){
        return index;
    }

    public void setMin(double v){
        minrend = v;
    }
}
