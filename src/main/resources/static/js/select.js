
function convertPriceToInteger(price) {
    return Math.round(price * 10000);
}
// 获取按钮元素
const mybutton = document.getElementById("topBtn");

// 当用户滚动超过 20px 时显示按钮
window.onscroll = function() {
    scrollFunction();
};

function scrollFunction() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        mybutton.style.display = "block";
    } else {
        mybutton.style.display = "none";
    }
}

// 当用户点击按钮时，返回页面顶部
function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}
document.getElementById('searchButton').addEventListener('click', function() {
    // 1. 获取用户选择的查询条件
    const type = document.getElementById('type').value;
    const energyType = document.getElementById('energyType').value;
    const minPrice = parseInt(document.getElementById('minPrice').value);
    const maxPrice = parseInt(document.getElementById('maxPrice').value);
    const sortBy = document.getElementById('sortBy').value;

    // 2. 转换价格为整数
    const minPriceInt = convertPriceToInteger(minPrice);
    const maxPriceInt = convertPriceToInteger(maxPrice);
    // 2. 构建查询条件的 JSON 对象
    const queryData = {
        type: type,
        energyType: energyType,
        minPrice: minPriceInt,
        maxPrice: maxPriceInt,
        sortBy: sortBy,
    };

    // 3. 发送 AJAX 请求到后端 API
    fetch('/query', { // 假设你的后端 API 端点是 /query, 使用POST
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(queryData)
    })
        .then(response => {
            if (response.status === 204) {
                // 处理没有内容的情况
                return [];
            } else if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            // 5. 处理返回的数据并更新页面
            const tableBody = document.getElementById('resultsTable').getElementsByTagName('tbody')[0];
            tableBody.innerHTML = ''; // 清空表格

            if (data.length === 0) {
                document.getElementById('no-results').style.display = 'block';
            } else {
                document.getElementById('no-results').style.display = 'none';
                data.forEach(car => {
                    const row = tableBody.insertRow();
                    const imageCell = row.insertCell();
                    const nameCell = row.insertCell();
                    const ratingCell = row.insertCell();
                    const minPriceCell = row.insertCell();
                    const maxPriceCell = row.insertCell();

                    // 创建图片元素并设置属性
                    const img = document.createElement('img');
                    img.src = car.imageUrl;
                    img.alt = car.carName;
                    imageCell.appendChild(img);

                    nameCell.textContent = car.carName;
                    ratingCell.textContent = car.rating;
                    minPriceCell.textContent = car.minPrice;
                    maxPriceCell.textContent = car.maxPrice;
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 可以添加代码来显示错误信息给用户
        });
});
