package rs.raf.projekat1.sava_ivkovic_rn1220.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.ChangePasswordActivity;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button changepassbt;
    private Button logoutbt;
    private TextView usernametv;
    private TextView emailtv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        changepassbt = view.findViewById(R.id.button5);
        logoutbt = view.findViewById(R.id.button6);
        usernametv = view.findViewById(R.id.textView3);
        emailtv = view.findViewById(R.id.textView4);

        loadData(view);

        logoutbt.setOnClickListener(e->{
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putInt("login", -1)
                    .apply();
            Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
            this.getActivity().finish();
        });

        changepassbt.setOnClickListener(e->{
            Intent intent = new Intent(this.getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loadData(View view){
        try{
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            int id = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), MODE_PRIVATE).getInt("login", -1);
            Cursor result = database.rawQuery("SELECT Username, Email FROM Login WHERE id = " + id + ";", null);
            result.moveToFirst();
            usernametv.setText(result.getString(0));
            emailtv.setText(result.getString(1));
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}