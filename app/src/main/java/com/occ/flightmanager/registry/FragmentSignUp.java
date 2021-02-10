package com.occ.flightmanager.registry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.occ.flightmanager.R;

public class FragmentSignUp extends Fragment {

    EditText usernameTxt, passwordTxt, password2Txt;
    Button directToSignIn, sign_up_button;
    CheckBox cbTerms;
    SQLiteDatabase database;

    public FragmentSignUp() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        directToSignIn = view.findViewById(R.id.directButton_si);
        sign_up_button = view.findViewById(R.id.signUpButton);
        usernameTxt = view.findViewById(R.id.editTextUsername_su);
        passwordTxt = view.findViewById(R.id.editTextUserPassword_su);
        password2Txt = view.findViewById(R.id.editTextUserPassword2_su);
        cbTerms = view.findViewById(R.id.termsCheckBox_su);

        directToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view);
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(view);
            }
        });
    }

    public void signUp(View view) {
        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String password2 = password2Txt.getText().toString();
        String db  = getResources().getString(R.string.db_name);
        String queryTable = getResources().getString(R.string.query_create_table);
        String queryAdd = getResources().getString(R.string.query_add);
        String queryCheck =getResources().getString(R.string.query_check);
        boolean isChecked = cbTerms.isChecked();

        if (!password.matches(password2))
            Toast.makeText(view.getContext(), getResources().getString(R.string.passwords_does_not_match), Toast.LENGTH_SHORT).show();
        else if (password.isEmpty() || password2.isEmpty() || username.isEmpty())
            Toast.makeText(view.getContext(), getResources().getString(R.string.fields_empty), Toast.LENGTH_SHORT).show();
        else if (!isChecked)
            Toast.makeText(view.getContext(), getResources().getString(R.string.accept_terms), Toast.LENGTH_SHORT).show();
        else {
            try {
                database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE, null);
                database.execSQL(queryTable);
                Cursor cursor = database.rawQuery(queryCheck, new String[]{username});
                if (cursor.getCount() == 0) {
                    SQLiteStatement statement = database.compileStatement(queryAdd);
                    statement.bindString(1, username);
                    statement.bindString(2, password);
                    statement.execute();
                    Toast.makeText(view.getContext(), getResources().getString(R.string.sign_up_successful), Toast.LENGTH_SHORT).show();
                    navigate(view);
                } else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.username_error), Toast.LENGTH_SHORT).show();
                cursor.close();
                database.close();
            } catch (Exception ex) {
                Toast.makeText(view.getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void navigate(View view) {
        NavDirections navDirections = FragmentSignUpDirections.actionFragmentSingUpToFragmentSignIn();
        Navigation.findNavController(view).navigate(navDirections);
    }
}
