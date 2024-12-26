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
document.getElementById("searchButton").addEventListener("click", function() {
    // 获取表单输入值
    let type = document.getElementById("type").value;
    let energyType = document.getElementById("energyType").value;
    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    let sortBy = document.getElementById("sortBy").value;
    const sortOrder = document.getElementById("sortOrder").value;
    if (type === "") {
        type = null; // 将空字符串转为 null
    }
    if (energyType === "") {
        energyType = null; // 将空字符串转为 null
    }
    if (sortBy === "") {
        sortBy = null; // 将空字符串转为 null
    }
    // 创建查询条件对象
    const queryParams = {
        type: type,
        energyType: energyType,
        minPrice: minPrice,
        maxPrice: maxPrice,
        sortBy: sortBy,
        sortOrder: sortOrder
    };
    console.log(JSON.stringify(queryParams));
    // 发起请求到后端
    fetch('http://127.0.0.1:10009/query', {
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
            // 遍历返回的数据并将每一项插入到表格中
            data.forEach(vehicle => {
                const row = resultsTable.insertRow();

                // 添加车辆图片列
                const imageCell = row.insertCell(0);
                const image = document.createElement('img');
                image.src = vehicle.image;
                image.alt = vehicle.car_name;
                image.width = 200; // 设置图片宽度
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

                // 添加操作列
                const actionCell = row.insertCell(5);

                // 创建 "删除记录" 按钮
                const deleteButton = document.createElement('button');
                deleteButton.textContent = "删除记录";
                deleteButton.classList.add('delete-btn'); // 添加样式类
                deleteButton.addEventListener('click', function () {
                    deleteVehicle(vehicle.car_name); // 调用删除函数
                });

                // 创建 "修改记录" 按钮
                const updateButton = document.createElement('button');
                updateButton.textContent = "修改记录";
                updateButton.classList.add('update-btn'); // 添加样式类
                updateButton.addEventListener('click', function () {
                    updateVehicle(vehicle.car_name); // 调用修改函数
                });

                // 将按钮添加到操作列
                actionCell.appendChild(deleteButton);
                actionCell.appendChild(updateButton);
            });
        })
        .catch(error => {
            console.error('查询车辆时出错:', error);
        });
});


//删除函数
function deleteVehicle(carName) {
    // 获取 search-form 中的 type 和 energyType 的值
    const type = document.getElementById("type").value;
    const energyType = document.getElementById("energyType").value;

    // 获取当前登录用户的 userId
    const user_Id = sessionStorage.getItem('userId');

    // 检查是否获取到必要参数
    if (!user_Id) {
        alert("用户未登录，请先登录！");
        return;
    }

    if (confirm(`确定要删除车辆 "${carName}" 吗？`)) {
        // 构造请求体
        const requestBody = {
            car_name: carName,
            type: type, // 若为空字符串，则设置为 null
            energyType: energyType, // 若为空字符串，则设置为 null
            userId: user_Id,
        };
        console.log(JSON.stringify(requestBody));
        // 发起请求到后端
        fetch('http://127.0.0.1:10009/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody), // 将请求体序列化为 JSON
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("车辆删除成功！");
                    location.reload(); // 刷新页面以更新表格
                } else {
                    alert("删除失败：" + data.message);
                }
            })
            .catch(error => {
                console.error("删除车辆时出错：", error);
                alert("删除失败，请稍后重试。");
            });
    }
}

//更新函数
function updateVehicle(carName) {
    // 弹出输入框，获取新的车辆信息（仅作为示例，可以用更好的方式实现，比如弹窗）
    const newRating = prompt("请输入新的车辆评分：");
    const newMinPrice = prompt("请输入新的最低价格：");
    const newMaxPrice = prompt("请输入新的最高价格：");

    if (newRating && newMinPrice && newMaxPrice) {
        fetch('http://127.0.0.1:10009/updateVehicle', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                car_name: carName,
                new_rating: newRating,
                new_min_price: newMinPrice,
                new_max_price: newMaxPrice,
            }),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("车辆信息更新成功！");
                    location.reload(); // 刷新页面以更新表格
                } else {
                    alert("更新失败：" + data.message);
                }
            })
            .catch(error => {
                console.error("更新车辆信息时出错：", error);
                alert("更新失败，请稍后重试。");
            });
    } else {
        alert("输入无效，请重新操作！");
    }
}

// 增加车辆
document.getElementById('addCarBtn').addEventListener('click', function () {
    insertCar();
});

function insertCar() {
    // 获取弹窗中的输入值
    const carImage = document.getElementById('carImage').value.trim();
    const carName = document.getElementById('carName').value.trim();
    const carRating = parseFloat(document.getElementById('carRating').value.trim());
    const carMinPrice = parseFloat(document.getElementById('carMinPrice').value.trim());
    const carMaxPrice = parseFloat(document.getElementById('carMaxPrice').value.trim());
    const energyType = document.getElementById('energy_Type').value;
    const carType = document.getElementById('carType').value;
    const userId = sessionStorage.getItem('userId'); // 获取当前登录用户的 ID

    // 检查是否登录
    if (!userId) {
        alert("用户未登录，请先登录！");
        return;
    }

    // 验证输入是否完整
    if (!carName || isNaN(carRating) || isNaN(carMinPrice) || isNaN(carMaxPrice) || !energyType || !carType) {
        alert("请填写完整信息！");
        return;
    }

    // 构造请求体
    const requestBody = {
        image: carImage || null,
        carName: carName,
        rating: carRating,
        minPrice: carMinPrice,
        maxPrice: carMaxPrice,
        energyType: energyType,
        type: carType,
        userId: userId,
    };

    // 发起 POST 请求到 /insert 接口
    fetch('http://127.0.0.1:10009/insert', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
    })
        .then(response => response.json()) // 解析 JSON 响应
        .then(data => {
            if (data.success) {
                alert("车辆信息添加成功！");
                document.getElementById('addCarModal').style.display = 'none'; // 关闭弹窗
                location.reload(); // 刷新页面以显示最新的车辆列表
            } else {
                alert("添加失败：" + data.message);
            }
        })
        .catch(error => {
            console.error("添加车辆时出错：", error);
            alert("添加失败，请稍后重试。");
        });
}