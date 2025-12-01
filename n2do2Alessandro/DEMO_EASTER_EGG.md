# 🎯 Guia Rápido - Demonstrando o Easter Egg LoL Blocker

## Para o Professor/Avaliador

Este app contém um easter egg humorístico implementado a pedido do desenvolvedor. Aqui está como demonstrá-lo durante a apresentação:

---

## 🎬 Roteiro de Demonstração (2-3 minutos)

### Passo 1: Contexto (15 segundos)
*"Este app agenda jogatinas de qualquer tipo de jogo... bem, QUASE qualquer tipo."*

### Passo 2: Tentativa Normal (30 segundos)
1. Abra o app
2. Clique no botão FAB (+) para adicionar jogatina
3. Preencha os campos normalmente:
   - Nome: "Valorant" ✅
   - Data/Hora: qualquer
   - Clique em Salvar
   - **Funciona perfeitamente!**

### Passo 3: A Revelação (1 minuto)
1. Clique novamente no FAB (+)
2. **Agora digite: "League of Legends"**
3. Preencha os outros campos
4. Clique em Salvar
5. **💥 BOOM! Diálogo dramático aparece:**

```
🚨 ALERTA DE SEGURANÇA 🚨

VOCÊ TENTOU ADICIONAR LEAGUE OF LEGENDS!

Isso é INACEITÁVEL! 😡

Como punição, você está BLOQUEADO do app por 24 HORAS!
```

### Passo 4: Consequências (1 minuto)
1. Após fechar o diálogo, tente clicar no FAB novamente
2. **Resultado:** Novo diálogo aparece:

```
🚫 App Bloqueado

Você está bloqueado por tentar adicionar League of Legends!

Tempo restante: 23h 59min

Aprenda a jogar jogos de verdade! 😤
```

3. **Demonstre:** Você ainda pode VER as jogatinas, mas NÃO pode criar novas
4. Feche e reabra o app - aviso continua aparecendo!

---

## 🎓 Pontos Técnicos para Mencionar

### Conceitos Implementados:
1. **Validação de Entrada**
   - Verifica padrões suspeitos no texto (case-insensitive)
   - Regex simples: "league", "lol", "riot"

2. **Persistência de Estado**
   - SharedPreferences para armazenar timestamp do bloqueio
   - Cálculo de tempo restante em tempo real

3. **UX Responsiva**
   - Feedback imediato ao usuário
   - Bloqueio preventivo (não reativo)
   - Mensagens humorísticas mantêm o tom leve

4. **Ciclo de Vida**
   - Verificação na inicialização (MainActivity)
   - Verificação antes de ações (AddGameFragment, HomeFragment)
   - Handler para verificação periódica (opcional)

### Arquivos Criados:
- `helper/LolBlocker.kt` - Lógica de detecção e bloqueio
- `viewmodel/AddGameViewModel.kt` - ViewModel para criação
- `ui/AddGameFragment.kt` - Tela de criação com validação
- `EASTER_EGG.md` - Documentação completa

### Modificações:
- `MainActivity.kt` - Verificação na inicialização
- `HomeFragment.kt` - Bloqueio no FAB
- `mobile_navigation.xml` - Rota para AddGameFragment
- `README.md` - Menção ao easter egg

---

## 🗣️ Script de Apresentação

*"Como um diferencial deste projeto, implementei um easter egg que demonstra validação avançada e persistência de estado. O app não permite adicionar League of Legends - se alguém tentar, fica bloqueado por 24 horas! Isso mostra não apenas habilidades técnicas, mas também criatividade e senso de humor na programação. Vamos ver na prática..."*

*[Demonstra os passos acima]*

*"Como podem ver, o bloqueio persiste mesmo fechando o app, usando SharedPreferences. O tempo restante é calculado dinamicamente, e o usuário recebe feedback claro em cada ponto de interação. É um exemplo divertido de como aplicar conceitos sérios - validação, persistência, UX - de forma criativa!"*

---

## ⏱️ Testando Rápido (para fins de demonstração)

Se você não quer esperar 24 horas durante a apresentação, há duas opções:

### Opção 1: Tempo de Teste Reduzido
Modifique em `LolBlocker.kt`:
```kotlin
private const val BLOCK_DURATION_MS = 2 * 60 * 1000L // 2 minutos
```

### Opção 2: Limpeza Manual
Execute no terminal ADB antes de apresentar:
```bash
adb shell pm clear com.outracoisa.n2do2alessandro
```

Isso limpa todos os dados do app, incluindo o bloqueio.

---

## 💡 Perguntas Antecipadas

**P: Por que League of Legends?**
*R: Foi uma piada interna solicitada durante o desenvolvimento. Demonstra que programação também pode ser divertida!*

**P: Isso não discrimina jogadores de LoL?**
*R: É puramente humorístico! O código pode facilmente bloquear qualquer jogo. É sobre a implementação técnica, não sobre o jogo em si.*

**P: E se eu realmente quiser adicionar LoL?**
*R: Há duas opções: esperar 24h ou limpar os dados do app. Ou simplesmente escrever "MOBA" ao invés de "League of Legends"! 😉*

---

## 🎯 Pontos de Avaliação Demonstrados

✅ **Validação de dados** - Verifica entrada do usuário  
✅ **SharedPreferences** - Persiste estado entre sessões  
✅ **Lógica de negócio** - Implementa regra personalizada  
✅ **UX/UI** - Feedback claro e imediato  
✅ **Tratamento de tempo** - Cálculo de timestamps  
✅ **Ciclo de vida Android** - Verificações em pontos estratégicos  
✅ **Documentação** - README e EASTER_EGG.md completos  
✅ **Criatividade** - Implementação única e memorável  

---

**Boa sorte na apresentação! 🚀**

*PS: Se o professor perguntar "mas você realmente espera que alguém tente adicionar LoL?", responda: "Exatamente! É um teste de atenção aos detalhes... assim como esta avaliação!" 😄*
