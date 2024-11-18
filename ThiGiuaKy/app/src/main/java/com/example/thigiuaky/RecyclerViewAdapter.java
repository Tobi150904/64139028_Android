package com.example.thigiuaky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Student> students;

    public RecyclerViewAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.nameTextView.setText(student.getName());
        holder.ageTextView.setText("Age: " + student.getAge());
        holder.classTextView.setText("Class: " + student.getStudentClass());
        holder.skillsTextView.setText("Skills: " + student.getSkills());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ageTextView;
        public TextView classTextView;
        public TextView skillsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_student_name);
            ageTextView = itemView.findViewById(R.id.tv_student_age);
            classTextView = itemView.findViewById(R.id.tv_student_class);
            skillsTextView = itemView.findViewById(R.id.tv_student_skills);
        }
    }
}