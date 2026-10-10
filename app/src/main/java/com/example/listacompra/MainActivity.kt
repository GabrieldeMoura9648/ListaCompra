package com.example.listacompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listacompra.ui.theme.ListaCompraTheme

data class Produto(
    val nome: String,
    val preco: Double,
    val quantidade: Int
) {
    val totalItem: Double
        get() = preco * quantidade
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaCompraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaCompraScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListaCompraScreen(modifier: Modifier = Modifier) {
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0.0) }
    val produtos = remember { mutableStateListOf<Produto>() }

    val onCadastrar = {
        val precoDouble = preco.toDoubleOrNull() ?: 0.0
        val quantidadeInt = quantidade.toIntOrNull() ?: 0
        if (nome.isNotBlank() && precoDouble > 0 && quantidadeInt > 0) {
            val novoProduto = Produto(
                nome = nome,
                preco = precoDouble,
                quantidade = quantidadeInt
            )
            produtos.add(novoProduto)
            total = produtos.sumOf { it.preco * it.quantidade }
            nome = ""
            preco = ""
            quantidade = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Formulario(
            nome = nome,
            preco = preco,
            quantidade = quantidade,
            onNomeChange = { nome = it },
            onPrecoChange = { preco = it },
            onQuantidadeChange = { quantidade = it },
            onCadastrar = onCadastrar
        )

        Spacer(modifier = Modifier.height(16.dp))

        Resumo(total = total)

        Spacer(modifier = Modifier.height(16.dp))

        ListaProdutos(
            produtos = produtos,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Formulario(
    nome: String,
    preco: String,
    quantidade: String,
    onNomeChange: (String) -> Unit,
    onPrecoChange: (String) -> Unit,
    onQuantidadeChange: (String) -> Unit,
    onCadastrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = preco,
                onValueChange = onPrecoChange,
                label = { Text("Preço (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            OutlinedTextField(
                value = quantidade,
                onValueChange = onQuantidadeChange,
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onCadastrar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar Produto")
        }
    }
}

@Composable
fun Resumo(
    total: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total da Compra",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "R$ %.2f".format(total),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ListaProdutos(
    produtos: List<Produto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(produtos) { produto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = produto.nome,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${produto.quantidade}x R$ %.2f".format(produto.preco),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "R$ %.2f".format(produto.totalItem),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaCompraPreview() {
    ListaCompraTheme {
        ListaCompraScreen()
    }
}
