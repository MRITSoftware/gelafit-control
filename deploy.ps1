# Script PowerShell para fazer deploy inicial no GitHub
# Execute: .\deploy.ps1

Write-Host "🚀 Iniciando deploy para GitHub..." -ForegroundColor Cyan

# Verificar se Git está instalado
try {
    $gitVersion = git --version
    Write-Host "✅ Git encontrado: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Git não encontrado. Por favor, instale o Git primeiro." -ForegroundColor Red
    exit 1
}

# Verificar se estamos em um repositório Git
if (-not (Test-Path .git)) {
    Write-Host "📦 Inicializando repositório Git..." -ForegroundColor Yellow
    git init
}

# Verificar remote
$remote = git remote get-url origin 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "🔗 Adicionando remote do GitHub..." -ForegroundColor Yellow
    git remote add origin https://github.com/MRITSoftware/boot-receiver.git
} else {
    Write-Host "✅ Remote já configurado: $remote" -ForegroundColor Green
}

# Verificar status
Write-Host "`n📊 Status do repositório:" -ForegroundColor Cyan
git status --short

# Perguntar se deseja continuar
$response = Read-Host "`n❓ Deseja fazer commit e push? (S/N)"
if ($response -ne "S" -and $response -ne "s") {
    Write-Host "❌ Operação cancelada." -ForegroundColor Yellow
    exit 0
}

# Adicionar arquivos
Write-Host "`n📝 Adicionando arquivos..." -ForegroundColor Yellow
git add .

# Fazer commit
$commitMessage = Read-Host "`n💬 Mensagem do commit (ou pressione Enter para usar padrão)"
if ([string]::IsNullOrWhiteSpace($commitMessage)) {
    $commitMessage = "feat: Adiciona aplicativo Boot Receiver para Android Stick"
}

Write-Host "💾 Fazendo commit..." -ForegroundColor Yellow
git commit -m $commitMessage

# Verificar branch
$currentBranch = git branch --show-current
if ($currentBranch -eq "master") {
    Write-Host "🔄 Renomeando branch de 'master' para 'main'..." -ForegroundColor Yellow
    git branch -M main
    $currentBranch = "main"
}

# Fazer push
Write-Host "`n🚀 Fazendo push para GitHub..." -ForegroundColor Yellow
Write-Host "⚠️  Se for a primeira vez, você pode precisar fazer autenticação." -ForegroundColor Yellow

try {
    git push -u origin $currentBranch
    Write-Host "`n✅ Deploy concluído com sucesso!" -ForegroundColor Green
    Write-Host "`n📦 Os APKs serão gerados automaticamente pelo GitHub Actions." -ForegroundColor Cyan
    Write-Host "🔗 Acesse: https://github.com/MRITSoftware/boot-receiver/actions" -ForegroundColor Cyan
} catch {
    Write-Host "`n❌ Erro ao fazer push. Verifique suas credenciais do GitHub." -ForegroundColor Red
    Write-Host "💡 Dica: Use 'git config --global credential.helper wincred' para salvar credenciais" -ForegroundColor Yellow
    exit 1
}

Write-Host "`n✨ Pronto! O workflow do GitHub Actions irá gerar os APKs automaticamente." -ForegroundColor Green
