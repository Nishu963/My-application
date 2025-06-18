package com.example.myapplication53
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigator()
            }
        }
    }
}
data class Product(val name: String, val description: String, val imageUrl: String)
sealed class Screen {
    object Login : Screen()
    object Signup : Screen()
    object Home : Screen()
    data class QuotationForm(val product: Product) : Screen()
    object ThankYou : Screen()
}
@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    when (val screen = currentScreen) {
        is Screen.Login -> LoginScreen(
            onSignupClick = { currentScreen = Screen.Signup },
            onLoginSuccess = { currentScreen = Screen.Home }
        )
        is Screen.Signup -> SignupScreen(
            onLoginClick = { currentScreen = Screen.Login },
            onSignupSuccess = { currentScreen = Screen.Home }
        )
        is Screen.Home -> HomeScreen(
            onProductClick = { product -> currentScreen = Screen.QuotationForm(product) }
        )
        is Screen.QuotationForm -> QuotationFormScreen(
            product = screen.product,
            onSubmit = { currentScreen = Screen.ThankYou }
        )
        is Screen.ThankYou -> ThankYouScreen(
            onBackToHome = { currentScreen = Screen.Home }
        )
    }
}
@Composable
fun LoginScreen(onSignupClick: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) onLoginSuccess()
            }
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignupClick) {
            Text("Don't have an account? Sign up")
        }
    }
}
@Composable
fun SignupScreen(onLoginClick: () -> Unit, onSignupSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) onSignupSuccess()
            }
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onProductClick: (Product) -> Unit) {
    val products = listOf(
        Product("Aluminum Wire Rod", "High quality wire rod", "https://5.imimg.com/data5/SELLER/Default/2023/10/356882426/JD/DV/HL/143709516/zinc-wire-500x500.jpg"),
        Product("MIG Wire", "Premium MIG welding wire", "https://5.imimg.com/data5/SELLER/Default/2021/12/JC/HO/XZ/143709516/aluminium-binding-wire-500x500.jpeg"),
        Product("Aluminum Ingot", "Standard aluminum ingots", "https://5.imimg.com/data5/SELLER/Default/2022/1/AY/GO/JU/143709516/aluminum-wire-mig-wire-250x250.jpg"),
        Product("Aluminum Rod", "Round and flat rods", "https://5.imimg.com/data5/SELLER/Default/2021/12/JC/HO/XZ/143709516/aluminium-binding-wire-500x500.jpeg")
    )
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Aluminum Products") })
    }) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(product.name, fontSize = 18.sp)
                        Text(product.description, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuotationFormScreen(product: Product, onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Request Quotation") })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Product: ${product.name}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact Info") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && contact.isNotBlank()) onSubmit()
                }
            ) {
                Text("Submit Request")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThankYouScreen(onBackToHome: () -> Unit) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Thank You") })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Thank you for your quotation request!", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackToHome) { Text("Back to Home") }
        }
    }
}

