package com.example.itpreminder.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.itpreminder.R;
import com.example.itpreminder.activities.MainActivity;
import com.example.itpreminder.fragment.LoginFragment;

public class FragmentNavigation extends Fragment {
    public final static String TAG = FragmentNavigation.class.getSimpleName();

    private static int iMainActivityFragmentContainer;
    private static boolean bDoubleBackToExitPressedOnce = false;
    private static FragmentNavigation fragmentNavigationInstance;
    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;
    private static Handler handler = new Handler();

    public static FragmentNavigation getInstance(Context context) {
        if (fragmentNavigationInstance == null) {
            iMainActivityFragmentContainer = R.id.fragment_content;
            fragmentNavigationInstance = new FragmentNavigation();
            fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        }

        return fragmentNavigationInstance;
    }

    private void addFragment(Fragment fragment, int container) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment, fragment.getTag());
        fragmentTransaction.addToBackStack(null);
        try {
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment fragment, int container) {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment topFragment = fragmentManager.findFragmentById(container);
        if (topFragment == null) {
            // if there is nothing to replace, then add a new one:
            addFragment(fragment, container);
        } else {
            // if there is fragment to replace, then replace it:
            fragmentTransaction.replace(container, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            try {
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Fragment getCurrentFragment(int container) {
        return fragmentManager.findFragmentById(container);
    }

    public void onBackPressed(MainActivity activity) {
        // If Home page is open: double press exit:
        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof LoginFragment) {
            doublePressExit(activity);
            return;
        }

        /*if (getCurrentFragment(iMainActivityFragmentContainer) instanceof HomeFragment) {
            popBackStack();
            return;
        }

        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof ProfileFragment) {
            popBackStack();
            return;
        }

        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof TeamFragment) {
            popBackStack();
            return;
        }

        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof SettingsFragment) {
            popBackStack();
            return;
        }

        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof SalaryRaiseHistoryFragment) {
            popBackStack();
            return;
        }

        if (getCurrentFragment(iMainActivityFragmentContainer) instanceof FreeDaysHistoryFragment) {
            popBackStack();
            return;
        }*/

        //Other cases:
        activity.moveTaskToBack(true);
    }

    private void doublePressExit(MainActivity activity) {
        if (bDoubleBackToExitPressedOnce) {
            bDoubleBackToExitPressedOnce = false;
            activity.moveTaskToBack(true);
            return;
        }

        bDoubleBackToExitPressedOnce = true;
        Toast.makeText(activity, R.string.back_button_press, Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bDoubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void popBackStack() {
        fragmentManager.popBackStack();
    }
}
