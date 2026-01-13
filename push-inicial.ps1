# Script para fazer push inicial no GitHub
# Execute este script para enviar o código para o repositório

Write-Host "🚀 Configurando Git e fazendo push inicial..." -ForegroundColor Cyan
Write-Host ""

# Verificar se Git está instalado
try {
    $gitVersion = git --version
    Write-Host "✅ Git encontrado" -ForegroundColor Green
} catch {
    Write-Host "❌ Git não encontrado. Instale o Git primeiro: https://git-scm.com/" -ForegroundColor Red
    exit 1
}

# Inicializar Git se necessário
if (-not (Test-Path .git)) {
    Write-Host "📦 Inicializando repositório Git..." -ForegroundColor Yellow
    git init
} else {
    Write-Host "✅ Repositório Git já inicializado" -ForegroundColor Green
}

# Configurar remote
Write-Host ""
Write-Host "🔗 Configurando remote do GitHub..." -ForegroundColor Yellow
$remoteExists = git remote get-url origin 2>$null
if ($LASTEXITCODE -ne 0) {
    git remote add origin https://github.com/MRITSoftware/mrit-control.git
    Write-Host "✅ Remote adicionado" -ForegroundColor Green
} else {
    Write-Host "✅ Remote já configurado: $remoteExists" -ForegroundColor Green
    Write-Host "🔄 Atualizando URL do remote..." -ForegroundColor Yellow
    git remote set-url origin https://github.com/MRITSoftware/mrit-control.git
}

# Adicionar todos os arquivos
Write-Host ""
Write-Host "📝 Adicionando arquivos ao Git..." -ForegroundColor Yellow
git add .

# Verificar se há algo para commitar
$status = git status --porcelain
if ([string]::IsNullOrWhiteSpace($status)) {
    Write-Host "⚠️  Nenhuma mudança para commitar. Verificando se já existe commit..." -ForegroundColor Yellow
    $hasCommits = git log --oneline -1 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Nenhum commit encontrado. Criando commit inicial..." -ForegroundColor Yellow
        git commit -m "feat: Adiciona funcionalidade de reiniciar dispositivo via Supabase

- BroadcastReceiver para BOOT_COMPLETED
- Tela de seleção de app na primeira vez  
- Serviço para verificar internet e abrir app
- Retry automático quando não há internet
- Integração com Supabase para comandos remotos
- Reiniciar dispositivo via banco de dados
- Device Admin para controle de reinício
- Monitoramento automático de comandos
- Compatível com Android TV/Stick
- Workflow GitHub Actions para build automático
- Documentação completa incluída"
    }
} else {
    Write-Host "💾 Criando commit..." -ForegroundColor Yellow
    git commit -m "feat: Adiciona funcionalidade de reiniciar dispositivo via Supabase

- BroadcastReceiver para BOOT_COMPLETED
- Tela de seleção de app na primeira vez
- Serviço para verificar internet e abrir app
- Retry automático quando não há internet
- Integração com Supabase para comandos remotos
- Reiniciar dispositivo via banco de dados
- Device Admin para controle de reinício
- Monitoramento automático de comandos
- Compatível com Android TV/Stick
- Workflow GitHub Actions para build automático
- Documentação completa incluída"
}

# Verificar branch e renomear se necessário
$currentBranch = git branch --show-current
if ([string]::IsNullOrWhiteSpace($currentBranch)) {
    Write-Host "🌿 Criando branch main..." -ForegroundColor Yellow
    git checkout -b main
    $currentBranch = "main"
} elseif ($currentBranch -eq "master") {
    Write-Host "🔄 Renomeando branch de 'master' para 'main'..." -ForegroundColor Yellow
    git branch -M main
    $currentBranch = "main"
}

Write-Host ""
Write-Host "🚀 Fazendo push para GitHub..." -ForegroundColor Yellow
Write-Host "⚠️  Você pode precisar fazer autenticação (token ou senha)" -ForegroundColor Yellow
Write-Host ""

# Tentar fazer push
try {
    git push -u origin $currentBranch --force
    Write-Host ""
    Write-Host "✅ Push concluído com sucesso!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📦 O GitHub Actions irá gerar os APKs automaticamente!" -ForegroundColor Cyan
    Write-Host "🔗 Acesse: https://github.com/MRITSoftware/mrit-control/actions" -ForegroundColor Cyan
} catch {
    Write-Host ""
    Write-Host "❌ Erro ao fazer push. Possíveis causas:" -ForegroundColor Red
    Write-Host "   1. Você não tem permissão no repositório" -ForegroundColor Yellow
    Write-Host "   2. Precisa fazer autenticação (token GitHub)" -ForegroundColor Yellow
    Write-Host "   3. O repositório não existe ou está privado" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "💡 Soluções:" -ForegroundColor Cyan
    Write-Host "   - Use um Personal Access Token: https://github.com/settings/tokens" -ForegroundColor White
    Write-Host "   - Ou configure SSH: git remote set-url origin git@github.com:MRITSoftware/mrit-control.git" -ForegroundColor White
    exit 1
}

Write-Host ""
Write-Host "✨ Pronto! Seu código está no GitHub!" -ForegroundColor Green
