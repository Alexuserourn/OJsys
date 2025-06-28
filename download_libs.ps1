# 创建目录
New-Item -ItemType Directory -Force -Path "src/main/webapp/js/lib"

# 下载文件
$files = @{
    "https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.min.js" = "src/main/webapp/js/lib/vue.min.js"
    "https://code.jquery.com/jquery-3.1.1.min.js" = "src/main/webapp/js/lib/jquery.min.js"
    "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" = "src/main/webapp/js/lib/popper.min.js"
    "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" = "src/main/webapp/js/lib/bootstrap.min.js"
    "https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.9/ace.js" = "src/main/webapp/js/lib/ace.js"
    "https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.9/ext-language_tools.js" = "src/main/webapp/js/lib/ext-language_tools.js"
}

foreach ($file in $files.GetEnumerator()) {
    Write-Host "Downloading $($file.Key) to $($file.Value)"
    Invoke-WebRequest -Uri $file.Key -OutFile $file.Value
} 