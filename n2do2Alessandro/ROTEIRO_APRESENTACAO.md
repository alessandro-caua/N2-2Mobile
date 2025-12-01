# 🎮 Roteiro de Apresentação - App Jogatinas
## Projeto feito em Jetpack Compose + Room Database

---

## 📋 Índice
1. [Introdução (1 min)](#1-introdução)
2. [Demonstração do App (4-5 min)](#2-demonstração-do-app)
3. [Explicação do Código (10-15 min)](#3-explicação-do-código)
4. [Conclusão (1 min)](#4-conclusão)

---

## 1. Introdução

### O que falar:
> "Boa tarde, professor. Meu projeto é um app de **Jogatinas** - um banco de dados para guardar e organizar partidas de jogos com amigos. O usuário pode cadastrar qualquer jogo que quiser, definir data, hora e marcar quem vai participar. O app foi desenvolvido em **Kotlin com Jetpack Compose** e usa **Room Database** para persistência local."

### Tecnologias utilizadas:
- **Kotlin 2.0** - Linguagem principal
- **Jetpack Compose** - UI declarativa moderna do Android
- **Material Design 3** - Componentes visuais
- **Room Database** - Persistência SQLite com abstração
- **MVVM** - Arquitetura Model-View-ViewModel
- **Navigation Compose** - Navegação entre telas
- **Coroutines** - Operações assíncronas

---

## 2. Demonstração do App

### Passo a passo para demonstrar:

#### 2.1. Tela Inicial (HomeScreen)
> "Esta é a tela principal. Ela mostra todas as jogatinas cadastradas em cards. Cada card exibe o nome do jogo, data, horário, quantidade de jogadores e o status."

**Mostrar:**
- Lista vazia com mensagem "Nenhuma jogatina agendada"
- Botão flutuante (+) para adicionar

#### 2.2. Adicionar Jogatina (AddGameScreen)
> "Ao clicar no botão +, abrimos o formulário de cadastro. Aqui o usuário tem **total liberdade** para digitar qualquer jogo que quiser."

**Mostrar:**
- Campo de nome do jogo (texto livre)
- Seletor de data (DatePicker nativo)
- Seletor de horário (TimePicker nativo)
- Campo do anfitrião
- Máximo de jogadores
- Local (opcional)
- Observações (opcional)
- Seletor de cor do card

**Cadastrar um exemplo:**
- Jogo: "Banco Imobiliário"
- Data: hoje
- Hora: 19:00
- Anfitrião: "Alessandro"
- Max jogadores: 6

#### 2.3. Visualizar na Lista
> "Após salvar, a jogatina aparece na lista principal com todas as informações resumidas."

#### 2.4. Tela de Detalhes (DetailsScreen)
> "Clicando em um card, abrimos os detalhes completos. Aqui podemos alterar o status da jogatina."

**Mostrar:**
- Informações completas
- Clicar no status para mudar (Agendado → Realizado → Cancelado)
- Botão de excluir

#### 2.5. Easter Egg 🥚 (OPCIONAL - se quiser mostrar)
> "Ah, e tem um easter egg no app..."

**Demonstrar:**
- Tentar adicionar "League of Legends" ou "LoL"
- Mostrar o bloqueio de 24 horas
- Explicar que é uma brincadeira entre amigos

---

## 3. Explicação do Código

### 3.1. Estrutura do Projeto

```
app/src/main/java/com/outracoisa/n2do2alessandro/
├── dao/                    # Data Access Object (Room)
│   └── CardGameDao.kt
├── database/               # Configuração do banco
│   └── CardGameDatabase.kt
├── entity/                 # Modelo de dados
│   └── CardGameEntity.kt
├── helper/                 # Classes auxiliares
│   ├── CardGameConstants.kt
│   └── LolBlocker.kt       # Easter egg
├── repository/             # Camada de repositório
│   └── CardGameRepository.kt
├── viewmodel/              # ViewModels (MVVM)
│   ├── HomeViewModel.kt
│   ├── AddGameViewModel.kt
│   └── DetailsViewModel.kt
├── ui/
│   ├── navigation/         # Navegação Compose
│   │   └── AppNavigation.kt
│   ├── screens/            # Telas Composable
│   │   ├── HomeScreen.kt
│   │   ├── AddGameScreen.kt
│   │   ├── DetailsScreen.kt
│   │   └── BlockedScreen.kt
│   └── theme/              # Tema Material3
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt         # Activity principal
```

---

### 3.2. Entity (Modelo de Dados)

**Arquivo:** `entity/CardGameEntity.kt`

```kotlin
@Entity(tableName = "game_sessions")
data class GameSessionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    
    @ColumnInfo(name = "game_name")
    val gameName: String,           // Nome do jogo (livre)
    
    @ColumnInfo(name = "date")
    val date: String,               // Data: dd/MM/yyyy
    
    @ColumnInfo(name = "time")
    val time: String,               // Hora: HH:mm
    
    @ColumnInfo(name = "host_name")
    val hostName: String,           // Quem organizou
    
    @ColumnInfo(name = "players_confirmed")
    val playersConfirmed: String = "", // Lista de jogadores
    
    @ColumnInfo(name = "max_players")
    val maxPlayers: Int = 4,
    
    @ColumnInfo(name = "location")
    val location: String = "",
    
    @ColumnInfo(name = "notes")
    val notes: String = "",
    
    @ColumnInfo(name = "status")
    val status: String = "Agendado", // Agendado, Realizado, Cancelado
    
    @ColumnInfo(name = "color")
    val color: String = "#2196F3"   // Cor do card
)
```

> "A Entity define a estrutura da tabela no banco. Cada campo com `@ColumnInfo` vira uma coluna. O `@PrimaryKey(autoGenerate = true)` gera IDs únicos automaticamente."

---

### 3.3. DAO (Data Access Object)

**Arquivo:** `dao/CardGameDao.kt`

```kotlin
@Dao
interface GameSessionDao {
    
    // SELECT - Listar todas as sessões
    @Query("SELECT * FROM game_sessions ORDER BY date DESC, time DESC")
    fun getAllSessions(): LiveData<List<GameSessionEntity>>
    
    // SELECT - Buscar por ID
    @Query("SELECT * FROM game_sessions WHERE id = :id")
    fun getSessionById(id: Int): LiveData<GameSessionEntity>
    
    // INSERT - Criar nova sessão
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: GameSessionEntity): Long
    
    // UPDATE - Atualizar sessão
    @Update
    suspend fun update(session: GameSessionEntity)
    
    // DELETE - Remover sessão
    @Delete
    suspend fun delete(session: GameSessionEntity)
    
    // DELETE por ID
    @Query("DELETE FROM game_sessions WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    // UPDATE específico - Status
    @Query("UPDATE game_sessions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)
}
```

> "O DAO é uma interface que define as operações do banco. As anotações `@Query`, `@Insert`, `@Update` e `@Delete` mapeiam para SQL. O `suspend` indica que são funções assíncronas (coroutines). O `LiveData` permite observar mudanças em tempo real."

---

### 3.4. Database (Configuração Room)

**Arquivo:** `database/CardGameDatabase.kt`

```kotlin
@Database(
    entities = [GameSessionEntity::class], 
    version = 2, 
    exportSchema = false
)
abstract class GameSessionDatabase : RoomDatabase() {
    
    abstract fun gameSessionDao(): GameSessionDao
    
    companion object {
        @Volatile
        private var INSTANCE: GameSessionDatabase? = null
        
        fun getDatabase(context: Context): GameSessionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameSessionDatabase::class.java,
                    "game_sessions_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

> "Esta classe configura o Room Database. Usa o padrão Singleton para garantir uma única instância do banco. O `@Volatile` e `synchronized` garantem thread-safety."

---

### 3.5. Repository (Padrão Repository)

**Arquivo:** `repository/CardGameRepository.kt`

```kotlin
class GameSessionRepository(context: Context) {
    
    private val database = GameSessionDatabase.getDatabase(context)
    private val gameSessionDao = database.gameSessionDao()
    
    // READ - Listar todas
    fun getAllSessions(): LiveData<List<GameSessionEntity>> {
        return gameSessionDao.getAllSessions()
    }
    
    // CREATE - Inserir
    suspend fun insert(session: GameSessionEntity): Long {
        return gameSessionDao.insert(session)
    }
    
    // UPDATE - Atualizar
    suspend fun update(session: GameSessionEntity) {
        gameSessionDao.update(session)
    }
    
    // DELETE - Remover
    suspend fun delete(session: GameSessionEntity) {
        gameSessionDao.delete(session)
    }
    
    // UPDATE específico
    suspend fun updateStatus(id: Int, status: String) {
        gameSessionDao.updateStatus(id, status)
    }
}
```

> "O Repository é uma camada de abstração entre o ViewModel e o DAO. Se no futuro quisermos buscar dados de uma API, mudamos só aqui."

---

### 3.6. ViewModel (MVVM)

**Arquivo:** `viewmodel/HomeViewModel.kt`

```kotlin
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameSessionRepository(application.applicationContext)
    
    // LiveData observável pela UI
    val allSessions: LiveData<List<GameSessionEntity>> = repository.getAllSessions()
    
    fun insert(session: GameSessionEntity) {
        viewModelScope.launch {
            repository.insert(session)
        }
    }
    
    fun delete(session: GameSessionEntity) {
        viewModelScope.launch {
            repository.delete(session)
        }
    }
    
    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateStatus(id, status)
        }
    }
}
```

> "O ViewModel gerencia os dados da tela. O `viewModelScope.launch` executa operações em background sem travar a UI. O `LiveData` notifica automaticamente a tela quando os dados mudam."

---

### 3.7. Tela Composable - HomeScreen

**Arquivo:** `ui/screens/HomeScreen.kt`

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddClick: () -> Unit,
    onSessionClick: (Int) -> Unit,
    isBlocked: Boolean = false
) {
    // Observa os dados do banco em tempo real
    val sessions by viewModel.allSessions.observeAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf<GameSessionEntity?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎮 Jogatinas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        },
        floatingActionButton = {
            if (!isBlocked) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = PrimaryPurple
                ) {
                    Icon(Icons.Default.Add, "Adicionar")
                }
            }
        }
    ) { paddingValues ->
        if (sessions.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = sessions,
                    key = { session -> session.id }
                ) { session ->
                    GameSessionCard(
                        session = session,
                        onClick = { onSessionClick(session.id) },
                        onDeleteClick = { showDeleteDialog = session }
                    )
                }
            }
        }
    }
}
```

> "No Compose, a UI é declarativa. Usamos `observeAsState` para converter o LiveData em State do Compose. O `LazyColumn` é equivalente ao RecyclerView - renderiza só os itens visíveis. O `Scaffold` fornece a estrutura básica com TopBar e FAB."

---

### 3.8. Tela de Cadastro - AddGameScreen

**Arquivo:** `ui/screens/AddGameScreen.kt`

```kotlin
@Composable
fun AddGameScreen(
    viewModel: AddGameViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onGameBlocked: () -> Unit
) {
    val context = LocalContext.current
    
    // Estados do formulário
    var gameName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var hostName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#2196F3") }
    
    // DatePicker nativo
    val datePickerDialog = DatePickerDialog(context, { _, year, month, day ->
        date = String.format("%02d/%02d/%d", day, month + 1, year)
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    
    // Função de salvar
    fun validateAndSave() {
        // Validação dos campos...
        
        // Easter egg - verifica se é LoL
        if (LolBlocker.isGameNameSuspicious(gameName)) {
            LolBlocker.blockUser(context)
            Toast.makeText(context, "🚫 BLOQUEADO!", Toast.LENGTH_LONG).show()
            onGameBlocked()
            return
        }
        
        val session = GameSessionEntity(
            gameName = gameName,
            date = date,
            time = time,
            hostName = hostName,
            color = selectedColor
        )
        
        viewModel.insertGameSession(session,
            onSuccess = { onNavigateBack() },
            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        )
    }
    
    // UI com OutlinedTextField, DatePicker, ColorSelector...
    Column {
        OutlinedTextField(
            value = gameName,
            onValueChange = { gameName = it },
            label = { Text("Nome do Jogo *") },
            placeholder = { Text("Ex: Banco Imobiliário") }
        )
        // ... outros campos
        
        Button(onClick = { validateAndSave() }) {
            Text("Criar Jogatina")
        }
    }
}
```

> "Cada campo usa `remember { mutableStateOf() }` para manter o estado. Os DatePicker e TimePicker usam os dialogs nativos do Android. A validação e o easter egg acontecem antes de salvar."

---

### 3.9. Navegação Compose

**Arquivo:** `ui/navigation/AppNavigation.kt`

```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddGame : Screen("add_game")
    object Details : Screen("details/{sessionId}") {
        fun createRoute(sessionId: Int) = "details/$sessionId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    var isBlocked by remember { mutableStateOf(LolBlocker.isUserBlocked(context)) }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddClick = { navController.navigate(Screen.AddGame.route) },
                onSessionClick = { id -> 
                    navController.navigate(Screen.Details.createRoute(id))
                },
                isBlocked = isBlocked
            )
        }
        
        composable(Screen.AddGame.route) {
            AddGameScreen(
                onNavigateBack = { navController.popBackStack() },
                onGameBlocked = { isBlocked = true; navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
            DetailsScreen(
                sessionId = sessionId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

> "A navegação em Compose usa `NavHost` com rotas como strings. Cada `composable()` define uma tela. Parâmetros são passados na rota como `details/123`."

---

### 3.10. MainActivity

**Arquivo:** `MainActivity.kt`

```kotlin
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            JogatinasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
```

> "A MainActivity é simples no Compose. Usa `setContent` para definir a UI. O `JogatinasTheme` aplica as cores e tipografia. O `NavController` gerencia a navegação entre telas."

---

### 3.11. Tema Material 3

**Arquivo:** `ui/theme/Theme.kt`

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,      // #BB86FC
    secondary = AccentCyan,       // #03DAC6
    background = DarkBackground,  // #121212
    surface = DarkSurface,        // #1E1E1E
    surfaceVariant = DarkCard,    // #252525
    onPrimary = Color.Black,
    onBackground = TextPrimary,   // #FFFFFF
    onSurface = TextPrimary,
    error = ErrorRed              // #CF6679
)

@Composable
fun JogatinasTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
```

> "O tema define as cores do app. Usamos dark theme por padrão com cores Material You. O `MaterialTheme` aplica essas cores em todos os componentes automaticamente."

---

## 4. Conclusão

### Checklist de Requisitos ✅

| Requisito do Professor | Implementação |
|------------------------|---------------|
| Guardar jogos/partidas | ✅ Room Database com SQLite |
| Marcar jogatina | ✅ Formulário com data e hora |
| Cadastrar qualquer jogo | ✅ Campo de texto livre |
| Escolher data e hora | ✅ DatePicker + TimePicker |
| Salvar no banco | ✅ Room com CRUD completo |
| Marcar presença | ✅ Campo de jogadores confirmados |

### O que falar na conclusão:
> "O app atende todos os requisitos: permite cadastrar qualquer jogo, definir data e hora, e salva tudo no banco de dados local. A arquitetura MVVM separa bem as responsabilidades, e o Jetpack Compose torna a UI mais moderna e fácil de manter. Alguma dúvida?"

---

## 🎯 Perguntas que o Professor Pode Fazer

### P: "Por que usou Jetpack Compose em vez de XML?"
> "Compose é a tecnologia mais moderna do Android, recomendada pelo Google. A UI é declarativa, o código fica mais limpo e fácil de manter. É o padrão atual para novos projetos."

### P: "O que é o padrão MVVM?"
> "Model-View-ViewModel. O Model são os dados (Entity), a View é a UI (Composables), e o ViewModel faz a ponte entre eles. Isso separa responsabilidades e facilita testes."

### P: "Por que usar Room em vez de SQLite direto?"
> "Room é uma abstração sobre SQLite que evita código repetitivo. Ele valida queries em tempo de compilação, evitando erros em runtime. Também integra nativamente com LiveData e Coroutines."

### P: "O que é LiveData?"
> "É um observable que notifica a UI quando os dados mudam. Quando insiro uma jogatina no banco, a lista atualiza automaticamente sem precisar chamar refresh."

### P: "O que são Coroutines?"
> "São uma forma de executar operações assíncronas sem bloquear a thread principal. O `viewModelScope.launch` executa operações de banco em background."

### P: "O que é o Scaffold no Compose?"
> "É um componente que fornece a estrutura básica de uma tela: TopAppBar, BottomBar, FloatingActionButton, Drawer. Ele gerencia o layout automaticamente."

### P: "O que é LazyColumn?"
> "É o equivalente ao RecyclerView no Compose. Ele só renderiza os itens visíveis na tela, otimizando performance para listas grandes."

---

## 📊 Resumo Técnico

| Camada | Arquivo | Responsabilidade |
|--------|---------|-----------------|
| **Entity** | `CardGameEntity.kt` | Estrutura da tabela |
| **DAO** | `CardGameDao.kt` | Operações SQL |
| **Database** | `CardGameDatabase.kt` | Config do Room |
| **Repository** | `CardGameRepository.kt` | Abstração de dados |
| **ViewModel** | `*ViewModel.kt` | Lógica de negócio |
| **Screen** | `*Screen.kt` | UI Composable |
| **Navigation** | `AppNavigation.kt` | Rotas entre telas |
| **Theme** | `Theme.kt` | Cores e estilos |

---

**Boa apresentação!** 🎮🚀
