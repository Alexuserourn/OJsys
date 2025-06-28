/**
 * Online OJ System
 */
// 确保jQuery已正确加载
document.addEventListener('DOMContentLoaded', function() {
    if (typeof $ === 'undefined') {
        console.error('jQuery未加载，请检查js文件引入顺序');
        return;
    }

$(function () {
    //page scroll
    $('a.page-scroll').bind('click', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top - 100
        }, 1500);
        event.preventDefault();
    });

    //toggle scroll menu
    $(window).scroll(function () {
        var scroll = $(window).scrollTop();
        if (scroll >= 100) {
            $('.sticky-navigation').addClass('scrolled');
        } else {
            $('.sticky-navigation').removeClass('scrolled');
        }
    });
    });

    // 注册添加题目表单事件 - 确保表单存在
    const addProblemForm = document.getElementById('addProblemForm');
    if (addProblemForm) {
        addProblemForm.addEventListener('submit', function(event) {
            event.preventDefault();
            addProblem(event);
        });
    }
});

/**
 * 应用主要逻辑
 */

// 当前选中的题目
let currentProblem = null;

// 加载题目列表
function loadProblemList() {
    document.getElementById('problemList').style.display = 'block';
    document.getElementById('problemDetail').style.display = 'none';
    document.getElementById('problemForm').style.display = 'none';
    document.getElementById('userManagement').style.display = 'none';
    
    fetch('/problem')
        .then(response => response.json())
        .then(problems => {
            const tableBody = document.getElementById('problemsTable');
            tableBody.innerHTML = '';
            
            problems.forEach(problem => {
                const row = document.createElement('tr');
                
                const idCell = document.createElement('td');
                idCell.textContent = problem.id;
                
                const titleCell = document.createElement('td');
                titleCell.textContent = problem.title;
                
                const levelCell = document.createElement('td');
                levelCell.textContent = problem.level;
                
                const actionCell = document.createElement('td');
                
                // 查看按钮
                const viewBtn = document.createElement('button');
                viewBtn.className = 'btn btn-sm btn-view mr-2';
                viewBtn.textContent = '查看';
                viewBtn.addEventListener('click', function() {
                    loadProblemDetail(problem.id);
                });
                actionCell.appendChild(viewBtn);
                
                // 如果是教师，显示编辑和删除按钮
                if (isTeacher()) {
                    // 编辑按钮
                    const editBtn = document.createElement('button');
                    editBtn.className = 'btn btn-sm btn-edit mr-2';
                    editBtn.textContent = '编辑';
                    editBtn.addEventListener('click', function() {
                        editProblem(problem.id);
                    });
                    actionCell.appendChild(editBtn);
                    
                    // 删除按钮
                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'btn btn-sm btn-delete';
                    deleteBtn.textContent = '删除';
                    deleteBtn.addEventListener('click', function() {
                        deleteProblem(problem.id);
                    });
                    actionCell.appendChild(deleteBtn);
                }
                
                row.appendChild(idCell);
                row.appendChild(titleCell);
                row.appendChild(levelCell);
                row.appendChild(actionCell);
                
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('加载题目列表失败:', error);
            showMessage('error', '加载题目列表失败');
        });
}

// 加载题目详情
function loadProblemDetail(id) {
    console.log('正在加载题目ID:', id);
    
    fetch(`/problem?id=${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('题目加载失败，服务器返回: ' + response.status);
            }
            return response.json();
        })
        .then(problem => {
            console.log('获取到题目数据:', problem);
            
            if (!problem || !problem.id) {
                showMessage('error', '题目数据不完整');
                return;
            }
            
            currentProblem = problem;
            
            // 隐藏其他页面，显示题目详情页面
            document.getElementById('problemList').style.display = 'none';
            document.getElementById('problemDetail').style.display = 'block';
            document.getElementById('problemForm').style.display = 'none';
            document.getElementById('userManagement').style.display = 'none';
            
            // 更新题目信息
            document.getElementById('problemTitle').textContent = problem.title || '';
            document.getElementById('problemLevel').textContent = problem.level || '';
            document.getElementById('problemDescription').innerHTML = problem.description || '';
            document.getElementById('codeEditor').value = problem.templateCode || '';
            
            // 记录日志
            console.log('已加载题目详情:', problem);
        })
        .catch(error => {
            console.error('加载题目详情失败:', error);
            showMessage('error', '加载题目详情失败: ' + error.message);
        });
}

// 提交代码
function submitCode() {
    const code = document.getElementById('codeEditor').value;
    
    if (!code) {
        showMessage('error', '代码不能为空');
        return;
    }
    
    try {
        // 获取当前题目ID
        const problemId = parseInt(new URLSearchParams(window.location.search).get('id')) || 
                         (currentProblem ? currentProblem.id : null);
                         
        if (!problemId) {
            showMessage('error', '当前没有选中题目');
            return;
        }
        
        fetch('/compile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: problemId,
                code: code
            })
        })
        .then(response => response.json())
        .then(result => {
            document.getElementById('compileResult').textContent = result.reason || result.stdout;
            $('#resultModal').modal('show');
        })
        .catch(error => {
            console.error('提交代码失败:', error);
            showMessage('error', '提交代码失败');
        });
    } catch (error) {
        console.error('提交代码出错:', error);
        showMessage('error', '提交代码失败: ' + error.message);
    }
}

// 显示题目表单（仅教师可用）
function showProblemForm() {
    if (!isTeacher()) {
        showMessage('error', '权限不足');
        return;
    }
    
    document.getElementById('problemList').style.display = 'none';
    document.getElementById('problemDetail').style.display = 'none';
    document.getElementById('problemForm').style.display = 'block';
    document.getElementById('userManagement').style.display = 'none';
    
    // 重置表单
    document.getElementById('addProblemForm').reset();
}

// 显示题目列表
function showProblemList() {
    loadProblemList();
}

// 添加新题目（仅教师可用）
function addProblem(event) {
    event.preventDefault();
    
    if (!isTeacher()) {
        showMessage('error', '权限不足');
        return;
    }
    
    const title = document.getElementById('title').value;
    const level = document.getElementById('level').value;
    const description = document.getElementById('description').value;
    const templateCode = document.getElementById('templateCode').value;
    const testCode = document.getElementById('testCode').value;
    
    // 验证输入
    if (!title || !level || !description) {
        showMessage('error', '请填写所有必填字段');
        return;
    }
    
    // 构建请求数据
    const problemData = {
        title: title,
        level: level,
        description: description,
        templateCode: templateCode,
        testCode: testCode
    };
    
    // 日志记录提交的数据
    console.log("准备添加题目:", problemData);
    
    fetch('/problem', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(problemData)
    })
    .then(response => {
        // 详细记录响应信息
        console.log("添加题目响应状态:", response.status);
        console.log("添加题目响应头:", response.headers);
        
        if (!response.ok) {
            // 如果服务器返回错误状态码，尝试获取错误信息
            return response.text().then(text => {
                console.error("服务器返回错误:", text);
                try {
                    // 尝试解析为JSON
                    const errorData = JSON.parse(text);
                    return Promise.reject(errorData);
                } catch (e) {
                    // 如果不是JSON，返回原始文本
                    return Promise.reject({reason: `服务器错误 (${response.status}): ${text}`});
                }
            });
        }
        return response.json();
    })
    .then(result => {
        console.log("添加题目成功:", result);
        if (result.ok === 1) {
            showMessage('success', '添加题目成功');
            // 重置表单
            document.getElementById('addProblemForm').reset();
            // 延迟加载题目列表
            setTimeout(() => {
                loadProblemList();
            }, 1000);
        } else {
            showMessage('error', result.reason || '添加题目失败');
        }
    })
    .catch(error => {
        console.error('添加题目失败:', error);
        showMessage('error', error.reason || '添加题目失败，请检查服务器日志');
    });
}

// 删除题目（仅教师可用）
function deleteProblem(id) {
    if (!isTeacher()) {
        showMessage('error', '权限不足');
        return;
    }
    
    if (!confirm('确定要删除这道题目吗？')) {
        return;
    }
    
    fetch(`/problem?id=${id}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(result => {
        if (result.ok === 1) {
            showMessage('success', '删除题目成功');
            loadProblemList();
        } else {
            showMessage('error', result.reason || '删除题目失败');
        }
    })
    .catch(error => {
        console.error('删除题目失败:', error);
        showMessage('error', '删除题目失败');
    });
}

// 编辑题目（仅教师可用）
function editProblem(id) {
    if (!isTeacher()) {
        showMessage('error', '权限不足');
        return;
    }
    
    // 跳转到编辑页面
    window.location.href = `/edit-problem.html?id=${id}`;
}

// 显示添加用户模态框
function showAddUserModal() {
    // 重置表单
    document.getElementById('addUserForm').reset();
    // 默认选择学生角色
    document.getElementById('addRole').value = 'student';
    // 显示模态框
    $('#addUserModal').modal('show');
}

// 添加新用户
function addUser() {
    const username = document.getElementById('addUsername').value;
    const password = document.getElementById('addPassword').value;
    const role = document.getElementById('addRole').value;
    
    if (!username || !password) {
        showMessage('error', '用户名和密码不能为空');
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
            role
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage('success', '添加用户成功');
            $('#addUserModal').modal('hide');
            loadUserList();
        } else {
            showMessage('error', data.message || '添加用户失败');
        }
    })
    .catch(error => {
        console.error('添加用户失败:', error);
        showMessage('error', '添加用户失败');
    });
}

// 显示编辑用户模态框
function showEditUserModal(user) {
    document.getElementById('editUserId').value = user.id;
    document.getElementById('editUsername').value = user.username;
    document.getElementById('editPassword').value = '';
    document.getElementById('editRole').value = user.role;
    
    $('#editUserModal').modal('show');
}

// 更新用户信息
function updateUser() {
    const id = document.getElementById('editUserId').value;
    const username = document.getElementById('editUsername').value;
    const password = document.getElementById('editPassword').value;
    const role = document.getElementById('editRole').value;
    
    const userData = {
        id,
        username,
        role
    };
    
    // 只有在密码不为空时才更新密码
    if (password) {
        userData.password = password;
    }
    
    fetch(`/user/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            showMessage('success', '更新用户成功');
            $('#editUserModal').modal('hide');
            loadUserList();
        } else {
            showMessage('error', result.message || '更新用户失败');
        }
    })
    .catch(error => {
        console.error('更新用户失败:', error);
        showMessage('error', '更新用户失败');
    });
}

// 显示用户管理界面（仅教师可用）
function showUserManagement() {
    if (!isTeacher()) {
        showMessage('error', '权限不足');
        return;
    }
    
    document.getElementById('problemList').style.display = 'none';
    document.getElementById('problemDetail').style.display = 'none';
    document.getElementById('problemForm').style.display = 'none';
    document.getElementById('userManagement').style.display = 'block';
    
    loadUserList();
}

// 加载用户列表（仅教师可用）
function loadUserList() {
    if (!isTeacher()) {
        return;
    }
    
    fetch('/user')
        .then(response => response.json())
        .then(users => {
            const tableBody = document.getElementById('usersTable');
            tableBody.innerHTML = '';
            
            users.forEach(user => {
                const row = document.createElement('tr');
                
                const idCell = document.createElement('td');
                idCell.textContent = user.id;
                
                const usernameCell = document.createElement('td');
                usernameCell.textContent = user.username;
                
                const roleCell = document.createElement('td');
                roleCell.textContent = user.role === 'teacher' ? '教师' : '学生';
                
                const actionCell = document.createElement('td');
                
                // 编辑按钮
                const editBtn = document.createElement('button');
                editBtn.className = 'btn btn-sm btn-edit mr-2';
                editBtn.textContent = '编辑';
                editBtn.addEventListener('click', function() {
                    showEditUserModal(user);
                });
                actionCell.appendChild(editBtn);
                
                // 如果不是当前用户，显示删除按钮
                const currentUser = getCurrentUser();
                if (currentUser && currentUser.id !== user.id) {
                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'btn btn-sm btn-delete';
                    deleteBtn.textContent = '删除';
                    deleteBtn.addEventListener('click', function() {
                        deleteUser(user.id);
                    });
                    actionCell.appendChild(deleteBtn);
                }
                
                row.appendChild(idCell);
                row.appendChild(usernameCell);
                row.appendChild(roleCell);
                row.appendChild(actionCell);
                
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('加载用户列表失败:', error);
            showMessage('error', '加载用户列表失败');
        });
}

// 删除用户
function deleteUser(id) {
    if (!confirm('确定要删除这个用户吗？')) {
        return;
    }
    
    fetch(`/user/${id}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            showMessage('success', '删除用户成功');
            loadUserList();
        } else {
            showMessage('error', result.message || '删除用户失败');
        }
    })
    .catch(error => {
        console.error('删除用户失败:', error);
        showMessage('error', '删除用户失败');
    });
}

// 检查当前用户是否是教师
function isTeacher() {
    const user = getCurrentUser();
    return user && user.role === 'teacher';
}

// 获取当前登录用户
function getCurrentUser() {
    try {
        return JSON.parse(sessionStorage.getItem('currentUser'));
    } catch (e) {
        return null;
    }
}

// 显示消息提示框
function showMessage(type, message) {
    const alertBox = document.getElementById('alertBox');
    if (!alertBox) {
        console.error('未找到alertBox元素');
        alert(message); // 如果没有找到alertBox，则使用alert作为后备方案
        return;
    }
    alertBox.className = type === 'success' ? 'alert alert-success' : 'alert alert-danger';
    alertBox.textContent = message;
    alertBox.style.display = 'block';
    
    // 3秒后自动隐藏
    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 3000);
}

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 添加测试代码以确认jQuery和Bootstrap正确加载
    console.log('jQuery版本:', typeof $ !== 'undefined' ? $.fn.jquery : '未加载');
    console.log('Bootstrap模态框:', typeof $.fn.modal !== 'undefined' ? '已加载' : '未加载');
    
    // 确保所有元素存在后再添加事件监听
    const addUserBtn = document.getElementById('addUserBtn');
    if (addUserBtn) {
        addUserBtn.addEventListener('click', showAddUserModal);
    }
    
    const editUserBtn = document.getElementById('editUserBtn');
    if (editUserBtn) {
        editUserBtn.addEventListener('click', updateUser);
    }
    
    // 确认页面加载完成
    console.log('页面DOM已加载完成，已初始化应用');
});