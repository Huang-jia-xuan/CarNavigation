const addCarModal = document.getElementById('addCarModal');
const closeModal = document.getElementById('closeModal');
const insertCarButton = document.getElementById('insertCar');
const addCarBtn = document.getElementById('addCarBtn');

// 打开弹窗
insertCarButton.addEventListener('click', function() {
    addCarModal.style.display = 'block';
});

// 关闭弹窗
closeModal.addEventListener('click', function() {
    addCarModal.style.display = 'none';
});

// 查询按钮事件监听器
document.getElementById("addCarBtn").addEventListener("click", function() {
    // 获取表单输入值
    const carImage = document.getElementById("carImage").value;
    const carName = document.getElementById("carName" ).value;
    const carRating = document.getElementById("carRating").value;
    const carMinPrice = document.getElementById("carMinPrice").value;
    const carMaxPrice = document.getElementById("carMaxPrice").value;
    const userId = sessionStorage.getItem("userId")
    const queryParams = {
        userId:userId,
        carImage:carImage,
        carName:carName,
        carRating:carRating,
        carMinPrice:carMinPrice,
        carMaxPrice:carMaxPrice
    };
    console.log(JSON.stringify(queryParams));
    // 发起请求到后端
    fetch('http://127.0.0.1:10009/updateVehicle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(queryParams)
    })
        .then(response => {
            // 检查是否响应为空
            if (!response.ok) {
                throw new Error("请求失败，状态码：" + response.status);
            }
            return response.json();  // 解析 JSON 数据
        })
        .then(data => {
            console.log('返回的数据:', data);  // 打印数据
            // 获取结果表格的 tbody 部分
            const resultsTable = document.getElementById("resultsTable").getElementsByTagName('tbody')[0];
            // 清空表格
            resultsTable.innerHTML = '';

            // 如果没有结果，显示提示信息
            if (data.length === 0) {
                document.getElementById("no-results").style.display = 'block';
                return;
            } else {
                document.getElementById("no-results").style.display = 'none';
            }

            // 遍历返回的数据并将每一项插入到表格中
            data.forEach(vehicle => {
                const row = resultsTable.insertRow();

                // 添加车辆图片列
                const imageCell = row.insertCell(0);
                const image = document.createElement('img');
                image.src = vehicle.image;
                image.alt = vehicle.car_name;
                image.width = 200;  // 设置图片宽度
                imageCell.appendChild(image);

                // 添加车辆名称列
                const nameCell = row.insertCell(1);
                nameCell.textContent = vehicle.car_name;
                nameCell.width = 200;
                // 添加车辆评分列
                const ratingCell = row.insertCell(2);
                ratingCell.textContent = vehicle.rating;

                // 添加最低价格列
                const minPriceCell = row.insertCell(3);
                minPriceCell.textContent = vehicle.min_price + ' 万';

                // 添加最高价格列
                const maxPriceCell = row.insertCell(4);
                maxPriceCell.textContent = vehicle.max_price + ' 万';
            });
        })
        .catch(error => {
            console.error('查询车辆时出错:', error);
        });
});
