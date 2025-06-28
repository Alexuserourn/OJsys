/**
 * 处理用户认证相关的功能
 */

// 检查用户是否已登录
function checkLogin() {
    // 先尝试从sessionStorage获取用户信息
    const storedUser = sessionStorage.getItem('currentUser');
    if (storedUser) {
        try {
            const user = JSON.parse(storedUser);
            if (user && user.id) {
                // 如果sessionStorage中有有效的用户信息，直接更新UI
                updateUIAfterLogin(user);
                return;
            }
        } catch (e) {
            console.error('解析sessionStorage中的用户信息出错:', e);
            sessionStorage.removeItem('currentUser');
        }
    }

    // 如果sessionStorage中没有用户信息，则从服务器获取
    fetch('/user/current')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 用户已登录
                const user = data.user;
                // 保存用户信息到sessionStorage
                sessionStorage.setItem('currentUser', JSON.stringify(user));
                updateUIAfterLogin(user);
            } else {
                // 用户未登录，显示登录界面
                showLoginForm();
            }
        })
        .catch(error => {
            console.error('检查登录状态出错:', error);
            showLoginForm();
        });
}

// 登录后更新UI
function updateUIAfterLogin(user) {
    // 隐藏登录表单
    document.getElementById('loginContainer').style.display = 'none';
    
    // 显示用户信息
    const userInfoContainer = document.getElementById('userInfo');
    userInfoContainer.style.display = 'block';
    document.getElementById('username').textContent = user.username;
    document.getElementById('userRole').textContent = user.role === 'teacher' ? '教师' : '学生';
    
    // 根据用户角色显示或隐藏管理功能
    const adminControls = document.querySelectorAll('.admin-only');
    const studentControls = document.querySelectorAll('.student-only');
    
    if (user.role === 'teacher') {
        adminControls.forEach(el => el.style.display = 'block');
        studentControls.forEach(el => el.style.display = 'none');
    } else {
        adminControls.forEach(el => el.style.display = 'none');
        studentControls.forEach(el => el.style.display = 'block');
    }
    
    // 显示题目列表或其他内容
    loadProblemList();
}

// 显示登录表单
function showLoginForm() {
    // 直接跳转到登录页面，而不是在当前页面显示登录表单
    window.location.href = '/login.html';
}

// 执行登录
function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    if (!username || !password) {
        showMessage('error', '用户名和密码不能为空');
        return;
    }
    
    // 显示加载中的消息
    showMessage('info', '登录中，请稍候...');
    
    // 设置超时处理
    const loginTimeout = setTimeout(() => {
        showMessage('error', '登录请求超时，请重试');
    }, 10000); // 10秒超时
    
    fetch('/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    })
    .then(response => {
        clearTimeout(loginTimeout);
        return response.json();
    })
    .then(data => {
        if (data.success) {
            showMessage('success', '登录成功');
            // 保存用户信息到sessionStorage
            sessionStorage.setItem('currentUser', JSON.stringify(data.user));
            updateUIAfterLogin(data.user);
        } else {
            showMessage('error', data.message || '登录失败');
        }
    })
    .catch(error => {
        clearTimeout(loginTimeout);
        console.error('登录出错:', error);
        showMessage('error', '登录请求出错，请稍后重试');
    });
}

// 执行注册
function register() {
    const username = document.getElementById('registerUsername').value;
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (!username || !password) {
        showMessage('error', '用户名和密码不能为空');
        return;
    }
    
    if (password !== confirmPassword) {
        showMessage('error', '两次输入的密码不一致');
        return;
    }
    
    fetch('/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
            username, 
            password,
            role: 'student' // 默认注册为学生
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage('success', '注册成功，请登录');
            // 切换到登录表单
            showLoginTab();
        } else {
            showMessage('error', data.message || '注册失败');
        }
    })
    .catch(error => {
        console.error('注册出错:', error);
        showMessage('error', '注册请求出错');
    });
}

// 注销
function logout() {
    fetch('/user/logout')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 清除sessionStorage中的用户信息
                sessionStorage.removeItem('currentUser');
                showMessage('success', '注销成功');
                // 延迟一秒跳转，让用户看到成功消息
                setTimeout(() => {
                    showLoginForm();
                }, 1000);
            } else {
                showMessage('error', data.message || '注销失败');
            }
        })
        .catch(error => {
            console.error('注销出错:', error);
            showMessage('error', '注销请求出错');
        });
}

// 显示登录标签页
function showLoginTab() {
    document.getElementById('loginTab').classList.add('active');
    document.getElementById('registerTab').classList.remove('active');
    document.getElementById('loginForm').style.display = 'block';
    document.getElementById('registerForm').style.display = 'none';
}

// 显示注册标签页
function showRegisterTab() {
    document.getElementById('registerTab').classList.add('active');
    document.getElementById('loginTab').classList.remove('active');
    document.getElementById('registerForm').style.display = 'block';
    document.getElementById('loginForm').style.display = 'none';
}

// 显示消息提示
function showMessage(type, message) {
    const alertBox = document.getElementById('alertBox');
    alertBox.textContent = message;
    alertBox.className = 'alert';
    
    if (type === 'success') {
        alertBox.classList.add('alert-success');
    } else if (type === 'error') {
        alertBox.classList.add('alert-danger');
    }
    
    alertBox.style.display = 'block';
    
    // 3秒后自动隐藏
    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 3000);
}

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 检查登录状态
    checkLogin();
    
    // 注册登录表单事件
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        login();
    });
    
    // 注册注册表单事件
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();
        register();
    });
    
    // 注册登出按钮事件
    document.getElementById('logoutBtn').addEventListener('click', function(e) {
        e.preventDefault();
        logout();
    });
    
    // 注册标签页切换事件
    document.getElementById('loginTab').addEventListener('click', function(e) {
        e.preventDefault();
        showLoginTab();
    });
    
    document.getElementById('registerTab').addEventListener('click', function(e) {
        e.preventDefault();
        showRegisterTab();
    });
}); 