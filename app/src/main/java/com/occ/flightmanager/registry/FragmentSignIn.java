package com.occ.flightmanager.registry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.occ.flightmanager.drawer.rest.HomeActivity;
import com.occ.flightmanager.R;

public class FragmentSignIn extends Fragment {

    EditText usernameTxt, passwordTxt;
    Button sign_in_button,directToSignUp;
    SQLiteDatabase database;

    public FragmentSignIn(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        directToSignUp = view.findViewById(R.id.directButton_su);
        sign_in_button = view.findViewById(R.id.signInButton);
        usernameTxt = view.findViewById(R.id.editTextUsername_si);
        passwordTxt = view.findViewById(R.id.editTextUserPassword_si);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view);
            }
        });

        directToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentSignInDirections.ActionFragmentSignInToFragmentSingUp navDirections = FragmentSignInDirections.actionFragmentSignInToFragmentSingUp(getResources().getString(R.string.empty));
                Navigation.findNavController(view).navigate(navDirections);
            }
        });
    }

    public void signIn(View view){

        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String db = getResources().getString(R.string.db_name);
        String query = getResources().getString(R.string.query_sign_in);

        if(password.isEmpty() || username.isEmpty())
            Toast.makeText(view.getContext(),getResources().getString(R.string.fields_empty),Toast.LENGTH_SHORT).show();
        else{
            try{
                database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
                Cursor cursor =  database.rawQuery(query,new String[] {username,password});

                if(cursor.getCount()>0) {
                    Intent intent = new Intent(view.getContext(), HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(view.getContext(),getResources().getString(R.string.sign_in_successful),Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(view.getContext(),getResources().getString(R.string.wrong_cred),Toast.LENGTH_SHORT).show();

                cursor.close();
                database.close();
            }catch (Exception ex) {
                Toast.makeText(view.getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}