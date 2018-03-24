import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Deposit {

    public SimpleStringProperty memo;
    public SimpleStringProperty date;
    public SimpleDoubleProperty grossIncome;
    public SimpleDoubleProperty netIncome;

    /////////// CONSTRUCTORS ///////////////////////////////
    public Deposit(){
        this.memo = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
        this.grossIncome = new SimpleDoubleProperty(0);
        this.netIncome = new SimpleDoubleProperty(0);
    }

    public Deposit(String memo, String date, double grossIncome, double netIncome) {
        setMemo(memo);
        setDate(date);
        setGrossIncome(grossIncome);
        setNetIncome(netIncome);
    }

    /////////// GETTERS AND SETTERS ///////////////////////
    public String getMemo() {
        return memo.get();
    }

    public SimpleStringProperty memoProperty() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo.set(memo);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public double getGrossIncome() {
        return grossIncome.get();
    }

    public SimpleDoubleProperty grossIncomeProperty() {
        return grossIncome;
    }

    public void setGrossIncome(double grossIncome) {
        this.grossIncome.set(grossIncome);
    }

    public double getNetIncome() {
        return netIncome.get();
    }

    public SimpleDoubleProperty netIncomeProperty() {
        return netIncome;
    }

    public void setNetIncome(double netIncome) {
        this.netIncome.set(netIncome);
    }
}

