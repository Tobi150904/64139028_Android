package vn.ngoviethoang.quizappgui;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
    private String question;
    private String selectedAnswer;

    public Answer(String question, String selectedAnswer) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
    }

    protected Answer(Parcel in) {
        question = in.readString();
        selectedAnswer = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(selectedAnswer);
    }
}