package com.example.project_seg2105;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CourseList extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> course;

    public CourseList(Activity context, List<Course> course){
        super(context, R.layout.course_info, course);
        this.context = context;
        this.course = course;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.course_info, null, true);
        TextView courseCode = (TextView) listViewItem.findViewById(R.id.courseInfo);

        Course c = course.get(position);
        courseCode.setText(c.getCourseCode() + " " + c.getCourseName());
        return listViewItem;
    }

}
