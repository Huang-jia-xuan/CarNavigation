function loginUser() {
    const userId = document.getElementById('userId').value;
    const userPassword = document.getElementById('userPassword').value;

    if (!userId || !userPassword) {
        document.getElementById('message').textContent = "请填写完整信息！";
        return;
    }

    fetch('http://127.0.0.1:10009/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: userId,
            password: userPassword,
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 登录成功，存储用户 ID 和用户身份（admin 或 user）
                sessionStorage.setItem('userId', userId);
                sessionStorage.setItem('isLoggedIn', 'true');
                sessionStorage.setItem('userType', data.userType); // 存储用户类型

                document.getElementById('message').textContent = "登录成功！";
                window.location.href = 'index.html';  // 登录成功后跳转到首页
            } else {
                document.getElementById('message').textContent = "登录失败：" + data.message;
            }
        })
        .catch(error => {
            document.getElementById('message').textContent = "请求出错，请稍后重试。";
            console.error('Error:', error);
        });
}
