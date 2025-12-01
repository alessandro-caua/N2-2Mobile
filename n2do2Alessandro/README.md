# 🎮 Jogatinas - Agendador de Partidas

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Room](https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=google&logoColor=white)

**Aplicativo Android para agendar e gerenciar jogatinas de QUALQUER tipo de jogo com amigos**

[Funcionalidades](#-funcionalidades) • [Tecnologias](#-tecnologias) • [Arquitetura](#-arquitetura) • [Como Usar](#-como-usar)

</div>

---

## 📱 Sobre o Projeto

**Jogatinas** é um aplicativo Android desenvolvido para facilitar o agendamento e gerenciamento de partidas de **qualquer tipo de jogo** com grupos de amigos. Suporta:

- 🎮 **Videogames** - Valorant, Counter-Strike, Fortnite, etc *(League of Legends não é permitido! 🚫)*
- 🎲 **Jogos de Tabuleiro** - Catan, War, Monopoly, etc
- 🃏 **Jogos de Cartas** - Uno, Poker, Magic, Yu-Gi-Oh!, etc
- 🎭 **RPG de Mesa** - D&D, Pathfinder, Tormenta, etc
- ⚽ **Esportes** - Futebol, Basquete, Vôlei, etc
- 🎉 **Party Games** - Gartic, Stop, Detetive, etc

> **⚠️ AVISO:** Este app contém um **easter egg secreto**! Tente adicionar League of Legends e veja o que acontece... 😈
> 
> *(Dica: Leia [EASTER_EGG.md](EASTER_EGG.md) para entender a piada!)*

Com uma interface moderna e intuitiva, você pode:

- ✅ Criar jogatinas com data, horário e local
- 👥 Gerenciar confirmações de presença
- ⭐ Marcar jogatinas favoritas
- 🎨 Personalizar com cores para cada sessão
- 📊 Acompanhar histórico de partidas

Este projeto foi desenvolvido como parte da avaliação N2 da disciplina de **Desenvolvimento Mobile**, implementando todos os requisitos de CRUD com banco de dados Room.

---

## ✨ Funcionalidades

### 🏠 Tela Principal
- Lista todas as jogatinas agendadas
- Visualização em cards coloridos e modernos
- Status visual: Agendado, Realizado ou Cancelado
- Contagem de jogadores confirmados
- Botão de confirmação rápida de presença

### ⭐ Favoritos
- Lista especial das jogatinas marcadas como favoritas
- Acesso rápido às partidas mais importantes
- Interface clean com empty state personalizado

### 📝 Detalhes da Jogatina
- Informações completas: jogo, data, horário, local
- Lista de jogadores confirmados
- Observações e notas
- Opções para editar ou excluir

### ➕ Criar Jogatina
- Formulário intuitivo com validação
- Seletor de data e horário
- Campo de local opcional
- Escolha de cor para personalização
- Definição de máximo de jogadores

---

## 🛠️ Tecnologias

### Linguagem e Framework
- **Kotlin 2.0.21** - Linguagem moderna e concisa
- **Android SDK 24+** (compatível com 95%+ dos dispositivos)
- **Material Design 3** - Componentes visuais modernos

### Banco de Dados
- **Room 2.6.1** - Abstração SQLite com LiveData
- **KSP** - Processamento de anotações
- **Coroutines** - Operações assíncronas

### Arquitetura e Componentes
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** - Camada de abstração de dados
- **LiveData** - Observação reativa de dados
- **ViewBinding** - Binding seguro de views
- **Navigation Component** - Navegação entre telas

### UI/UX
- **RecyclerView** - Listas eficientes
- **Material CardView** - Cards modernos
- **Chips** - Indicadores de status
- **TextInputLayout** - Campos de entrada elegantes
- **ConstraintLayout** - Layouts responsivos

---

## 🏗️ Arquitetura

```
app/
├── data/
│   ├── entity/          # Entidades do banco de dados
│   ├── dao/             # Data Access Objects
│   ├── database/        # Configuração do Room
│   └── repository/      # Camada de repositório
├── viewmodel/           # ViewModels (MVVM)
├── ui/
│   ├── adapter/         # Adapters do RecyclerView
│   ├── listener/        # Interfaces de callback
│   └── fragments/       # Fragmentos da UI
└── helper/              # Constantes e utilitários
```

### Fluxo de Dados (MVVM)

```
┌─────────────┐
│   Fragment  │ ◄─── Observa LiveData
└──────┬──────┘
       │ Chama métodos
┌──────▼──────┐
│  ViewModel  │ ◄─── Gerencia UI State
└──────┬──────┘
       │ Requisita dados
┌──────▼──────┐
│ Repository  │ ◄─── Abstração de dados
└──────┬──────┘
       │ Acessa
┌──────▼──────┐
│     DAO     │ ◄─── Queries SQL
└──────┬──────┘
       │ Persiste
┌──────▼──────┐
│ Room DB     │ ◄─── SQLite
└─────────────┘
```

---

## 💾 Banco de Dados

### Tabela: `game_sessions`

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | INTEGER | Chave primária (auto-incremento) |
| `game_name` | TEXT | Nome do jogo |
| `date` | TEXT | Data (dd/MM/yyyy) |
| `time` | TEXT | Horário (HH:mm) |
| `host_name` | TEXT | Organizador da jogatina |
| `players_confirmed` | TEXT | Lista de jogadores (separados por vírgula) |
| `max_players` | INTEGER | Máximo de jogadores |
| `location` | TEXT | Local da partida |
| `notes` | TEXT | Observações |
| `status` | TEXT | Status (Agendado/Realizado/Cancelado) |
| `color` | TEXT | Cor do card (hex) |

### Operações CRUD

#### ✅ CREATE (Inserção)
```kotlin
suspend fun insert(session: GameSessionEntity): Long
```

#### 📖 READ (Consulta)
```kotlin
fun getAllSessions(): LiveData<List<GameSessionEntity>>
fun getUpcomingSessions(): LiveData<List<GameSessionEntity>>
fun getCompletedSessions(): LiveData<List<GameSessionEntity>>
fun getSessionById(id: Int): LiveData<GameSessionEntity>
```

#### ✏️ UPDATE (Atualização)
```kotlin
suspend fun update(session: GameSessionEntity)
suspend fun updatePlayers(id: Int, players: String)
suspend fun updateStatus(id: Int, status: String)
```

#### 🗑️ DELETE (Remoção)
```kotlin
suspend fun delete(session: GameSessionEntity)
suspend fun deleteById(id: Int)
```

---

## 🎨 Design System

### Paleta de Cores

- **Primary**: `#6C63FF` (Roxo vibrante)
- **Accent**: `#FF6584` (Rosa suave)
- **Background**: `#F5F7FA` (Cinza claro)

### Cores de Status
- 🟢 **Agendado**: Azul (`#2196F3`)
- ✅ **Realizado**: Verde (`#4CAF50`)
- ❌ **Cancelado**: Vermelho (`#F44336`)

### Cores de Jogos
8 cores vibrantes disponíveis para personalização dos cards

---

## 🚀 Como Usar

### Requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17+
- Gradle 8.7+
- Dispositivo/Emulador Android 7.0+ (API 24)

### Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/alessandro-caua/N2-2Mobile.git
cd N2-2Mobile/n2do2Alessandro
```

2. **Abra no Android Studio**
   - File → Open → Selecione a pasta `n2do2Alessandro`

3. **Sincronize as dependências**
   - Aguarde o Gradle sync automático
   - Ou clique em "Sync Now" se aparecer

4. **Execute o app**
   - Conecte um dispositivo ou inicie um emulador
   - Clique em ▶️ Run (Shift + F10)

### Build Manual
```bash
./gradlew assembleDebug
```

O APK será gerado em: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📖 Guia de Uso

### Criar Nova Jogatina

1. Na tela principal, toque no botão **+** (FAB)
2. Preencha os campos:
   - **Nome do Jogo**: Ex: "League of Legends 🎮", "Catan 🎲", "Uno 🃏", "D&D - Ravenloft 🎭"
   - **Seu Nome**: Organizador da partida
   - **Data**: Selecione no calendário
   - **Horário**: Escolha a hora
   - **Local** (opcional): Ex: "Casa do João", "Online - Discord", "República"
   - **Máx. Jogadores**: Número máximo
   - **Observações** (opcional): Detalhes extras sobre o tipo de jogo
3. Escolha uma **cor** para o card
4. Toque em **"Criar Jogatina"**

**Exemplos de Jogatinas:**
- 🎮 "League of Legends - Ranked 5v5" (Online - Discord)
- 🎲 "Catan - Torneio" (Casa do Pedro)
- 🃏 "Poker Texas Hold'em" (Clube)
- 🎭 "D&D - Campanha de Ravenloft" (Casa do DM)
- ⚽ "Futebol Society" (Quadra do Bairro)

### Confirmar Presença

1. Na lista de jogatinas, localize a partida
2. Toque em **"Confirmar"** no card
3. A contagem de jogadores será atualizada

### Marcar como Favorita

1. Toque em uma jogatina para abrir os detalhes
2. Marque o checkbox **"⭐ Marcar como favorito"**
3. A jogatina aparecerá na aba Favoritas

### Editar ou Excluir

1. Abra os detalhes da jogatina
2. Use os botões **✏️ Editar** ou **🗑️ Excluir**

---

## 🎯 Requisitos Atendidos (N2)

- ✅ Banco de dados Room configurado
- ✅ CRUD completo implementado
- ✅ Padrão MVVM com Repository
- ✅ Interface moderna com Material Design
- ✅ Navegação entre telas
- ✅ Operações assíncronas com Coroutines
- ✅ LiveData para observação reativa
- ✅ Documentação completa

---

## 👨‍💻 Desenvolvedor

**Alessandro Cauã**

- GitHub: [@alessandro-caua](https://github.com/alessandro-caua)
- Projeto: Avaliação N2 - Desenvolvimento Mobile

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.

---

<div align="center">

**Feito com ❤️ e ☕ por Alessandro**

</div>
