package ru.ok.itmo.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.ok.itmo.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val bindings: ActivityMainBinding by lazy {
        ActivityMainBinding.bind(findViewById(android.R.id.content))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = supportFragmentManager.backStackEntryCount.toString()
        bindings.startButton.setOnClickListener {
            supportFragmentManager.commit {
                replace(android.R.id.content, BaseFragment.newInstance())
            }
        }
        Navigator.setShouldCreateInnerFragment {
            with(supportFragmentManager) {
                if (backStackEntryCount == 0) return@setShouldCreateInnerFragment true
                if (getBackStackEntryAt(backStackEntryCount - 1).name == it) return@setShouldCreateInnerFragment false
                !popBackStackImmediate(it, 0)
            }
        }
        with(supportFragmentManager) {
            addOnBackStackChangedListener {
                supportActionBar?.title = backStackEntryCount.toString()
            }
            addOnBackStackChangedListener {
                if (backStackEntryCount == 0) return@addOnBackStackChangedListener
                popBackStack(getBackStackEntryAt(backStackEntryCount - 1).name, 0)
            }
        }
    }
}