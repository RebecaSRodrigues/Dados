package ifsp.android.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ifsp.Configuracao
import ifsp.android.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>

    private var configuracaoGlobal: Configuracao = Configuracao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        geradorRandomico = Random(System.currentTimeMillis())

        activityMainBinding.jogarDadoBt.setOnClickListener {
            if (configuracaoGlobal.numeroFaces > 6) {
                mostrarDado(configuracaoGlobal.numeroFaces)
            } else if (configuracaoGlobal.numeroDados == 1) {
                mostrarDado(configuracaoGlobal.numeroFaces)
                activityMainBinding.resultado2Iv.visibility = View.GONE
            }
            mostrarDado(configuracaoGlobal.numeroFaces, configuracaoGlobal.numeroDados)
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                if(result.data != null) {
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)
                    if (configuracao != null) {
                        configuracaoGlobal = configuracao
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingsMi) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

    private fun mostrarDado(numFaces: Int, numDados: Int = 1) {
        if (numFaces > 6) {
            activityMainBinding.resultado2Iv.visibility = View.GONE
            activityMainBinding.resultadoIv.visibility = View.GONE
        } else {
            activityMainBinding.resultadoIv.visibility = View.VISIBLE
            if(numDados == 2) {
                activityMainBinding.resultado2Iv.visibility = View.VISIBLE
            }
        }
        val resultado: Int = geradorRandomico.nextInt(1..numFaces)
        "A(s) face(s) sorteada(s) foi(ram) $resultado".also { activityMainBinding.resultadoTv.text = it }
        val nomeImagem = "dice_${resultado}"
        activityMainBinding.resultadoIv.setImageResource(
            resources.getIdentifier(nomeImagem, "mipmap", packageName)
        )
        if (numDados == 2) {
            val resultado2: Int = geradorRandomico.nextInt(1..numFaces)
            val nomeImagem2 = "dice_${resultado2}"
            activityMainBinding.resultado2Iv.setImageResource(
                resources.getIdentifier(nomeImagem2, "mipmap", packageName)
            )
            "A(s) face(s) sorteada(s) foi(ram) $resultado e $resultado2".also { activityMainBinding.resultadoTv.text = it }
        }
    }
}