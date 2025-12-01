# 🔧 Guia de Instalação do JDK 11+ para Compilar o Projeto

## ❌ Problema Atual

```
Dependency requires at least JVM runtime version 11. This build uses a Java 8 JVM.
```

Seu projeto está configurado corretamente, mas o sistema está usando Java 8. O Android Gradle Plugin 8.12.3 **requer JDK 11 ou superior**.

---

## ✅ Solução: Instalar e Configurar JDK 11+

### Passo 1: Baixar JDK

Escolha uma das opções:

#### Opção A: Amazon Corretto 11 (Recomendado)
- **Link**: https://corretto.aws/downloads/latest/amazon-corretto-11-x64-windows-jdk.msi
- **Motivo**: Gratuito, estável, mantido pela Amazon

#### Opção B: Oracle JDK 17 (LTS)
- **Link**: https://www.oracle.com/java/technologies/downloads/#java17-windows
- **Arquivo**: Windows x64 Installer (.exe)

#### Opção C: Eclipse Temurin 17
- **Link**: https://adoptium.net/temurin/releases/?version=17
- **Arquivo**: Windows x64 .msi

### Passo 2: Instalar JDK

1. Execute o instalador baixado
2. **IMPORTANTE**: Durante a instalação:
   - Marque a opção **"Set JAVA_HOME environment variable"**
   - Ou anote o caminho de instalação (ex: `C:\Program Files\Amazon Corretto\jdk11.0.XX`)

### Passo 3: Configurar Variáveis de Ambiente

#### Método Automático (Se o instalador configurou):
Pule para o Passo 4!

#### Método Manual:

1. **Abrir Configurações de Ambiente**:
   - Pressione `Win + Pause/Break`
   - Clique em "Configurações avançadas do sistema"
   - Clique em "Variáveis de Ambiente"

2. **Criar/Editar JAVA_HOME**:
   - Em "Variáveis do sistema", clique em "Novo" (ou "Editar" se já existir)
   - Nome: `JAVA_HOME`
   - Valor: `C:\Program Files\Amazon Corretto\jdk11.0.XX` (ajuste para sua instalação)
   - Clique em "OK"

3. **Atualizar PATH**:
   - Em "Variáveis do sistema", selecione `Path`
   - Clique em "Editar"
   - Clique em "Novo"
   - Adicione: `%JAVA_HOME%\bin`
   - **MOVA ESSA LINHA PARA O TOPO** (use os botões ↑↓)
   - Clique em "OK"

### Passo 4: Verificar Instalação

**Feche TODOS os terminais abertos** e abra um novo PowerShell:

```powershell
java -version
```

**Saída esperada**:
```
openjdk version "11.0.XX" 2023-XX-XX LTS
OpenJDK Runtime Environment Corretto-11.0.XX.X (build 11.0.XX+X-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.XX.X (build 11.0.XX+X-LTS, mixed mode)
```

Se ainda mostrar Java 8, você precisa:
1. Fechar **TODOS** os terminais (inclusive VS Code)
2. Reiniciar o VS Code
3. Testar novamente

### Passo 5: Compilar o Projeto

```powershell
cd "C:\Users\Aless\Documents\GitHub\N2-2Mobile\n2do2Alessandro"
.\gradlew.bat clean assembleDebug
```

---

## 🚀 Comandos Rápidos (Após Instalação)

### Limpar e Compilar:
```powershell
cd "C:\Users\Aless\Documents\GitHub\N2-2Mobile\n2do2Alessandro"
.\gradlew.bat clean assembleDebug
```

### Verificar Daemon do Gradle:
```powershell
.\gradlew.bat --status
```

### Parar Daemons Antigos:
```powershell
.\gradlew.bat --stop
```

---

## 🔍 Verificações Adicionais

### Verificar qual Java o Gradle está usando:
```powershell
.\gradlew.bat --version
```

**Saída esperada** deve incluir:
```
JVM:          11.0.XX (Amazon.com Inc. 11.0.XX+X-LTS)
```

### Se o Gradle ainda usar Java 8:

Crie/edite `gradle.properties` no diretório raiz do projeto:

```properties
org.gradle.java.home=C:\\Program Files\\Amazon Corretto\\jdk11.0.XX
```

*(Ajuste o caminho para sua instalação)*

---

## 📱 Após Compilação Bem-Sucedida

O APK estará em:
```
n2do2Alessandro\app\build\outputs\apk\debug\app-debug.apk
```

### Instalar no Emulador/Dispositivo:

```powershell
# Via ADB
adb install -r "app\build\outputs\apk\debug\app-debug.apk"

# Ou copie o arquivo e instale manualmente
```

---

## ❓ Troubleshooting

### Problema: "java não é reconhecido como comando"
**Solução**: PATH não está configurado corretamente. Repita o Passo 3.

### Problema: Ainda mostra Java 8 após instalação
**Solução**: 
1. Feche o VS Code completamente
2. Abra um novo PowerShell e verifique: `java -version`
3. Se correto, reabra o VS Code
4. Execute: `.\gradlew.bat --stop` (para parar daemons antigos)

### Problema: "Could not open settings generic class cache"
**Solução**: 
```powershell
.\gradlew.bat --stop
Remove-Item -Recurse -Force "$env:USERPROFILE\.gradle\caches"
.\gradlew.bat clean
```

### Problema: Múltiplas versões de Java instaladas
**Solução**: Use `gradle.properties` para especificar o caminho exato (ver acima)

---

## 📚 Referências

- [Android Gradle Plugin Requirements](https://developer.android.com/build/releases/gradle-plugin)
- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Gradle JVM Version](https://docs.gradle.org/current/userguide/compatibility.html)

---

## ✨ Checklist Final

- [ ] JDK 11+ instalado
- [ ] Variável JAVA_HOME configurada
- [ ] PATH atualizado
- [ ] `java -version` mostra 11+
- [ ] Todos os terminais fechados e reabertos
- [ ] VS Code reiniciado
- [ ] `.\gradlew.bat --stop` executado
- [ ] Compilação rodando!

**Boa sorte! 🚀**
