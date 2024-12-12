
function convertPriceToInteger(price) {
    return Math.round(price * 10000);
}

document.getElementById('searchButton').addEventListener('click', function() {
    // 1. 获取用户选择的查询条件
    const type = document.getElementById('type').value;
    const energyType = document.getElementById('energyType').value;
    const minPrice = parseInt(document.getElementById('minPrice').value);
    const maxPrice = parseInt(document.getElementById('maxPrice').value);
    const sortBy = document.getElementById('sortBy').value;
    const sortOrder = document.getElementById('sortOrder').value;

    // 2. 转换价格为整数
    const minPriceInt = convertPriceToInteger(minPrice);
    const maxPriceInt = convertPriceToInteger(maxPrice);
    // 2. 构建查询条件的 JSON 对象
    const queryData = {
        type: type,
        energyType: energyType,
        minPrice: minPrice,
        maxPrice: maxPrice,
        sortBy: sortBy,
    };
    if (sortOrder) {
        queryData.sortOrder = sortOrder;
    }

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
            // 4. 处理返回的数据并更新页面
            const tableBody = document.getElementById('resultsTable').getElementsByTagName('tbody')[0];
            tableBody.innerHTML = ''; // 清空表格

            if (data.length === 0) {
                document.getElementById('no-results').style.display = 'block';
            } else {
                document.getElementById('no-results').style.display = 'none';
                data.forEach(car => {
                    const row = tableBody.insertRow();
                    const nameCell = row.insertCell();
                    const minPriceCell = row.insertCell();
                    const maxPriceCell = row.insertCell();
                    const ratingCell = row.insertCell();
                    const imageCell = row.insertCell();

                    nameCell.textContent = car.carName;
                    minPriceCell.textContent = car.minPrice;
                    maxPriceCell.textContent = car.maxPrice;
                    ratingCell.textContent = car.rating;

                    const img = document.createElement('img');
                    img.src = car.image;
                    img.alt = car.carName;
                    imageCell.appendChild(img);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 可以添加代码来显示错误信息给用户
        });
});
