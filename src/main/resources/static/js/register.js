// register.js

// 始终显示管理员口令输入框
function showAdminPasswordField() {
    const adminPasswordContainer = document.getElementById('adminPasswordContainer');
    adminPasswordContainer.style.display = 'block';
}

// 注册用户逻辑
function registerUser() {
    const userId = document.getElementById('userId').value;
    const userName = document.getElementById('userName').value;
    const userPassword = document.getElementById('userPassword').value;
    const adminPassword = document.getElementById('adminPassword').value;

    if (!userId || !userName || !userPassword) {
        document.getElementById('message').textContent = "请填写完整信息！";
        return;
    }

    let role = 'user'; // 默认为用户

    // 如果输入了管理员口令，检查其是否正确
    if (adminPassword) {
        if (adminPassword === 'correctpassword') {
            role = 'admin'; // 口令正确，设置为管理员
        } else {
            document.getElementById('message').textContent = "管理员口令不正确！";
            return;
        }
    }

    // 向服务器发送注册请求
    fetch('http://127.0.0.1:10009/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: userId,
            name: userName,
            password: userPassword,
            role: role
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('message').textContent = "注册成功！";
                window.location.href = 'index.html';  // 登录成功后跳转到首页
            } else {
                document.getElementById('message').textContent = data.message;
            }
        })
        .catch(error => {
            document.getElementById('message').textContent = "请求出错，请稍后重试。";
            console.error('Error:', error);
        });
}

// 页面加载时始终显示管理员口令输入框
document.addEventListener('DOMContentLoaded', showAdminPasswordField);
