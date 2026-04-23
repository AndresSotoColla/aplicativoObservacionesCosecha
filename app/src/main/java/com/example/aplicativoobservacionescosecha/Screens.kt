package com.example.aplicativoobservacionescosecha

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainApp() {
    var currentScreen by rememberSaveable { mutableStateOf("Menu") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF9EBD7))) {
        when (currentScreen) {
            "Menu" -> {
                MainMenu(
                    onRegistroClick = { currentScreen = "Registro" },
                    onHistorialClick = { currentScreen = "Historial" },
                    onDescargarExcelClick = { ExportManager.exportToExcel(context) }
                )
            }
            "Registro" -> {
                BackHandler { currentScreen = "Menu" }
                RegistroScreen(
                    onBack = { currentScreen = "Menu" }
                )
            }
            "Historial" -> {
                BackHandler { currentScreen = "Menu" }
                HistorialScreen(
                    onBack = { currentScreen = "Menu" }
                )
            }
        }
    }
}

@Composable
fun MainMenu(
    onRegistroClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onDescargarExcelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Observaciones Cosecha",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Text(
            text = "Verificación y control",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.weight(1f))

        MenuButton(text = "Ingresar Registro", icon = Icons.Default.AddCircle, onClick = onRegistroClick)
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton(text = "Historial", icon = Icons.Default.History, onClick = onHistorialClick)
        Spacer(modifier = Modifier.height(16.dp))
        MenuButton(text = "Descargar Excel", icon = Icons.Default.Download, onClick = onDescargarExcelClick)

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF5E1C8),
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var bloque by rememberSaveable { mutableStateOf("") }
    var observaciones by rememberSaveable { mutableStateOf("") }

    var frutaDejada by rememberSaveable { mutableStateOf("0") }
    var descarteCamas by rememberSaveable { mutableStateOf("0") }
    var plantasSinParir by rememberSaveable { mutableStateOf("0") }
    var frutaJoven by rememberSaveable { mutableStateOf("0") }
    var frutaNoAprovechable by rememberSaveable { mutableStateOf("0") }
    var frutaAdelantada by rememberSaveable { mutableStateOf("0") }
    var coronas by rememberSaveable { mutableStateOf("0") }
    var frutaEnferma by rememberSaveable { mutableStateOf("0") }
    var mortalidad by rememberSaveable { mutableStateOf("0") }
    var quemaSol by rememberSaveable { mutableStateOf("0") }
    var danoMecanico by rememberSaveable { mutableStateOf("0") }
    var ausente by rememberSaveable { mutableStateOf("0") }
    var bajoPeso by rememberSaveable { mutableStateOf("0") }
    var golpeAgua by rememberSaveable { mutableStateOf("0") }
    var expandedBloque by remember { mutableStateOf(false) }

    val bloquesList = (1..87).map { "Bloque $it" }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
            }
            Text(
                "Verificación Cosecha",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedBloque,
                onExpandedChange = { expandedBloque = it }
            ) {
                OutlinedTextField(
                    value = bloque,
                    onValueChange = { bloque = it },
                    readOnly = false,
                    label = { Text("Bloque") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBloque) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black, unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedBloque,
                    onDismissRequest = { expandedBloque = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    bloquesList.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.Black) },
                            onClick = {
                                bloque = option
                                expandedBloque = false
                            }
                        )
                    }
                }
            }

            NumericFieldWithButtons("Fruta dejada por cosecha", frutaDejada) { frutaDejada = it }
            NumericFieldWithButtons("Descarte entre camas", descarteCamas) { descarteCamas = it }
            NumericFieldWithButtons("Plantas sin parir", plantasSinParir) { plantasSinParir = it }
            NumericFieldWithButtons("Fruta joven", frutaJoven) { frutaJoven = it }
            NumericFieldWithButtons("Fruta no aprovechable", frutaNoAprovechable) { frutaNoAprovechable = it }
            NumericFieldWithButtons("Fruta adelantada", frutaAdelantada) { frutaAdelantada = it }
            NumericFieldWithButtons("Coronas", coronas) { coronas = it }
            NumericFieldWithButtons("Fruta enferma", frutaEnferma) { frutaEnferma = it }
            NumericFieldWithButtons("Mortalidad", mortalidad) { mortalidad = it }
            NumericFieldWithButtons("Quema de sol", quemaSol) { quemaSol = it }
            NumericFieldWithButtons("Daño mecanico", danoMecanico) { danoMecanico = it }
            NumericFieldWithButtons("Ausente", ausente) { ausente = it }
            NumericFieldWithButtons("Bajo peso", bajoPeso) { bajoPeso = it }
            NumericFieldWithButtons("Golpe de agua", golpeAgua) { golpeAgua = it }

            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black, unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Button(
                onClick = {
                    if (bloque.isEmpty()) {
                        android.widget.Toast.makeText(context, "Por favor ingrese o seleccione un bloque", android.widget.Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val record = CosechaRecord(
                        bloque = bloque,
                        frutaDejada = frutaDejada.toIntOrNull() ?: 0,
                        descarteCamas = descarteCamas.toIntOrNull() ?: 0,
                        plantasSinParir = plantasSinParir.toIntOrNull() ?: 0,
                        frutaJoven = frutaJoven.toIntOrNull() ?: 0,
                        frutaNoAprovechable = frutaNoAprovechable.toIntOrNull() ?: 0,
                        frutaAdelantada = frutaAdelantada.toIntOrNull() ?: 0,
                        coronas = coronas.toIntOrNull() ?: 0,
                        frutaEnferma = frutaEnferma.toIntOrNull() ?: 0,
                        mortalidad = mortalidad.toIntOrNull() ?: 0,
                        quemaSol = quemaSol.toIntOrNull() ?: 0,
                        danoMecanico = danoMecanico.toIntOrNull() ?: 0,
                        ausente = ausente.toIntOrNull() ?: 0,
                        bajoPeso = bajoPeso.toIntOrNull() ?: 0,
                        golpeAgua = golpeAgua.toIntOrNull() ?: 0,
                        observaciones = observaciones
                    )
                    StorageManager.saveRecord(context, record)
                    android.widget.Toast.makeText(context, "Registro guardado correctamente", android.widget.Toast.LENGTH_SHORT).show()
                    
                    // Clear fields
                    bloque = ""
                    observaciones = ""
                    frutaDejada = "0"
                    descarteCamas = "0"
                    plantasSinParir = "0"
                    frutaJoven = "0"
                    frutaNoAprovechable = "0"
                    frutaAdelantada = "0"
                    coronas = "0"
                    frutaEnferma = "0"
                    mortalidad = "0"
                    quemaSol = "0"
                    danoMecanico = "0"
                    ausente = "0"
                    bajoPeso = "0"
                    golpeAgua = "0"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) {
                Text("Guardar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun NumericFieldWithButtons(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val current = value.toIntOrNull() ?: 0
                if (current > 0) onValueChange((current - 1).toString())
            }) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Menos", tint = Color.Black)
            }
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .width(80.dp)
                    .height(55.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Black, unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )
            IconButton(onClick = {
                val current = value.toIntOrNull() ?: 0
                onValueChange((current + 1).toString())
            }) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Mas", tint = Color.Black)
            }
        }
    }
}

@Composable
fun HistorialScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var records by remember { mutableStateOf(StorageManager.getRecords(context)) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
            }
            Text(
                "Historial de Registros",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        if (records.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay registros", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(records) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Fecha: ${dateFormat.format(Date(record.timestamp))}", fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Bloque: ${record.bloque}", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Total Dejada/Descarte: ${record.frutaDejada + record.descarteCamas}", fontSize = 14.sp, color = Color.Gray)
                            Text("Observaciones: ${record.observaciones.ifEmpty { "N/A" }}", fontSize = 14.sp, color = Color.Gray, maxLines = 1)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    StorageManager.deleteRecord(context, record.id)
                                    records = StorageManager.getRecords(context)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Text("Eliminar Registro")
                            }
                        }
                    }
                }
            }
        }
    }
}
