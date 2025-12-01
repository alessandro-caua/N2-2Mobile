# 🚫 Easter Egg: League of Legends Blocker

## O que é isso?

Este app contém um **easter egg humorístico** que bloqueia o usuário por **24 horas** se ele tentar adicionar League of Legends às jogatinas! 😤

## Como funciona?

### Detecção Automática

O sistema verifica automaticamente se o nome do jogo contém:
- "league"
- "lol"
- "league of legends"
- "riot"

*(não importa maiúsculas/minúsculas ou espaços extras)*

### O que acontece quando detectado?

1. **Punição Imediata** 🔨
   - Um diálogo dramático aparece informando a "VIOLAÇÃO"
   - O usuário é bloqueado por exatamente 24 horas
   - A tela de criação é fechada automaticamente

2. **Durante o Bloqueio** ⏰
   - O app mostra um aviso na inicialização
   - O botão FAB está bloqueado (mostra tempo restante)
   - O usuário pode VER as jogatinas mas NÃO pode criar novas

3. **Após o Desbloqueio** 🎉
   - Uma mensagem de "boas-vindas" aparece
   - Todas as funcionalidades são restauradas
   - O usuário aprendeu sua lição!

## Detalhes Técnicos

### Classes Envolvidas

1. **LolBlocker.kt** (`helper/`)
   - `isGameNameSuspicious(gameName)`: Detecta tentativas
   - `blockUser(context)`: Aplica o bloqueio
   - `isUserBlocked(context)`: Verifica status
   - `getBlockTimeRemaining(context)`: Calcula tempo restante
   - `formatBlockTime(timeMs)`: Formata em "Xh Ymin"

2. **AddGameFragment.kt**
   - Verifica bloqueio na abertura (onViewCreated)
   - Valida nome do jogo antes de salvar
   - Aplica bloqueio se detectado LoL

3. **HomeFragment.kt**
   - Verifica bloqueio ao clicar no FAB
   - Mostra diálogo com tempo restante

4. **MainActivity.kt**
   - Mostra aviso na inicialização se bloqueado
   - Verifica periodicamente o status
   - Celebra quando o bloqueio expira

### Armazenamento

- **SharedPreferences**: `JogatinasPrefs`
- **Chave**: `lol_block_until`
- **Valor**: Timestamp (Long) de quando o bloqueio expira
- **Duração**: `24 * 60 * 60 * 1000L` ms (exatamente 24 horas)

### Mensagens Humorísticas

**Ao detectar LoL:**
```
🚨 ALERTA DE SEGURANÇA 🚨

VOCÊ TENTOU ADICIONAR LEAGUE OF LEGENDS!

Isso é INACEITÁVEL! 😡

Como punição, você está BLOQUEADO do app por 24 HORAS!

Use esse tempo para refletir sobre suas escolhas de vida...

E da próxima vez, jogue algo de verdade! 🎮
```

**Durante o bloqueio (FAB):**
```
🚫 App Bloqueado

Você está bloqueado por tentar adicionar League of Legends!

Tempo restante: Xh Ymin

Aprenda a jogar jogos de verdade! 😤
```

**Na inicialização (se bloqueado):**
```
⚠️ Aviso Importante

Você está bloqueado por tentar adicionar League of Legends!

Você pode visualizar as jogatinas existentes, mas não pode criar novas.

Tempo restante: Xh Ymin

Use esse tempo para considerar seus erros... 😤
```

**Ao desbloquear:**
```
🎉 Você foi desbloqueado! Bem-vindo de volta!
```

## Como Testar

### Método 1: Uso Normal
1. Clique no FAB (+)
2. Digite "League of Legends" no campo de nome
3. Tente salvar
4. **BOOM!** Bloqueado! 🔨

### Método 2: Variações
Tente também:
- "lol"
- "LEAGUE"
- "riot games"
- "  LeAgUe Of LeGeNdS  "

Todas serão detectadas! 🕵️

### Método 3: Debug (para testar mais rápido)
Para testar sem esperar 24h, você pode:

1. Modificar temporariamente `BLOCK_DURATION_MS` em `LolBlocker.kt`:
```kotlin
private const val BLOCK_DURATION_MS = 60 * 1000L // 1 minuto (para testes)
```

2. Ou limpar manualmente no código:
```kotlin
// No AddGameFragment, adicione este botão secreto:
binding.someHiddenButton.setOnClickListener {
    getSharedPreferences("JogatinasPrefs", Context.MODE_PRIVATE)
        .edit()
        .remove("lol_block_until")
        .apply()
    Toast.makeText(context, "Bloqueio removido!", Toast.LENGTH_SHORT).show()
}
```

## Motivação

Este easter egg foi criado a pedido do usuário, que disse:

> "Tire League of Legends, coloque se o usuario colocar esse jogo ele nao consiga usar o app por 24 horas"

É uma piada interna sobre a reputação de LoL como um jogo viciante que consome muito tempo! 😄

## Desabilitando o Easter Egg

Se você quiser remover completamente esta funcionalidade:

1. Delete `LolBlocker.kt`
2. Remova as verificações em:
   - `AddGameFragment.saveGameSession()`
   - `HomeFragment.setupFab()`
   - `MainActivity.onCreate()`
3. Remova os imports de `LolBlocker`

Ou simplesmente comente a linha de verificação:
```kotlin
// if (LolBlocker.isGameNameSuspicious(gameName)) { ... }
```

## Avisos Legais

⚠️ **Isenção de responsabilidade:**
- Este é um easter egg HUMORÍSTICO
- Não promove discriminação contra jogadores de LoL
- É apenas uma piada entre amigos
- Todos os jogos são válidos (exceto LoL, aparentemente 😏)

---

**Lembre-se:** Este é um app educacional para avaliação N2. O easter egg demonstra:
- Validação de entrada
- Persistência com SharedPreferences
- Lógica de negócio personalizada
- UX responsiva (feedback ao usuário)
- Tratamento de tempo/data

E acima de tudo... um bom senso de humor! 🎮✨
