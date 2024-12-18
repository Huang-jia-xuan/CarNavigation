

var images = ['your-image-url.jpg', 'another-image-url.jpg', 'third-image-url.jpg'];
var currentImageIndex = 0;

function changeImage(direction) {
  currentImageIndex += direction;
  
  if (currentImageIndex < 0) {
    currentImageIndex = images.length - 1;
}
  
if (currentImageIndex > images.length - 1) {
currentImageIndex = 0; 
}
document.getElementById('imageContainer').style.backgroundImage = 'url(' + images[currentImageIndex] + ')';
}

let timeout;

      function showDropdown(index) {
        clearTimeout(timeout);
        document.getElementById(`dropdown${index}`).style.display = 'block';
        document.getElementById(`dropdown${index}`).style.opacity = 1;
        document.getElementById(`dropdown${index}`).style.visibility = 'visible';
      }

      function hideDropdown(index) {
        timeout = setTimeout(() => {
          document.getElementById(`dropdown${index}`).style.display = 'none';
          document.getElementById(`dropdown${index}`).style.opacity = 0;
          document.getElementById(`dropdown${index}`).style.visibility = 'hidden';
        }, 500); // 延迟时间
      }

      let topButton = document.getElementById("topBtn");

      // 当用户向下滚动100px时，显示按钮
      window.onscroll = function() {scrollFunction()};

      function scrollFunction() {
        if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
          topButton.style.display = "block";
        } else {
          topButton.style.display = "none";
        }
      }

      // 点击按钮时，返回顶部
      function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
      }


// function changeColor(color) {
//   let backgroundColor, navColor, siteNameColor, boxColor;
//   switch(color) {
//     case 'red':
//       boxColor = '#ffafaf';
//       backgroundColor = '#ffcccc';
//       navColor = '#ff6666';
//       siteNameColor = '#ff3333';
//       break;
//     case 'green':
//       boxColor = '#aaffaa';
//       backgroundColor = '#ccffcc';
//       navColor = '#66ff66';
//       siteNameColor = '#33ff33';
//       break;
//     default:
//       boxColor = '#fdf181';
//       backgroundColor = '#fcf29d';
//       navColor = '#fff8be';
//       siteNameColor = '#fffad1';
//   }
//
//   document.body.style.backgroundColor = backgroundColor;
//   document.querySelector('.navbar').style.backgroundColor = navColor;
//   document.querySelector('.site-name').style.backgroundColor = siteNameColor;
//
//   // 修改所有现有背景颜色的元素
//   document.querySelectorAll('.dropdown-content').forEach(function(element) {
//     element.style.backgroundColor = navColor;
//   });
//
//   document.querySelectorAll('.contact-info, .definition-box, .ranking-box, .box').forEach(function(element) {
//     element.style.backgroundColor = boxColor;
//   });
//
//   document.querySelectorAll('.ranking-table th').forEach(function(element) {
//     element.style.backgroundColor = navColor;
//   });
//
//   // 保存颜色设置到 localStorage
//   localStorage.setItem('backgroundColor', backgroundColor);
//   localStorage.setItem('navColor', navColor);
//   localStorage.setItem('siteNameColor', siteNameColor);
//   localStorage.setItem('boxColor', boxColor);
// }

// // 读取并应用颜色设置
// function applySavedColor() {
//   let backgroundColor = localStorage.getItem('backgroundColor');
//   let navColor = localStorage.getItem('navColor');
//   let siteNameColor = localStorage.getItem('siteNameColor');
//   let boxColor = localStorage.getItem('boxColor');
//
//   if (backgroundColor && navColor && siteNameColor && boxColor) {
//     document.body.style.backgroundColor = backgroundColor;
//     document.querySelector('.navbar').style.backgroundColor = navColor;
//     document.querySelector('.site-name').style.backgroundColor = siteNameColor;
//
//     document.querySelectorAll('.dropdown-content').forEach(function(element) {
//       element.style.backgroundColor = navColor;
//     });
//
//     document.querySelectorAll('.contact-info, .definition-box, .ranking-box, .box').forEach(function(element) {
//       element.style.backgroundColor = boxColor;
//     });
//
//     document.querySelectorAll('.ranking-table th').forEach(function(element) {
//       element.style.backgroundColor = navColor;
//     });
//   }
// }
//
// // 在页面加载时应用保存的颜色设置
// document.addEventListener('DOMContentLoaded', applySavedColor);




      