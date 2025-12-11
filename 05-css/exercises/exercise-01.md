# Exercise: Style a Responsive Card Component

## Objective
Create a responsive product card component using CSS3, including Flexbox/Grid layout, transitions, and media queries.

## Requirements

Style a product card (`product-card.html` and `styles.css`) with the following features:

### 1. Card Structure
```html
<div class="card">
    <img src="product.jpg" alt="Product" class="card-image">
    <div class="card-content">
        <span class="card-badge">New</span>
        <h3 class="card-title">Product Name</h3>
        <p class="card-description">Product description goes here...</p>
        <div class="card-price">
            <span class="price-original">$99.99</span>
            <span class="price-sale">$79.99</span>
        </div>
        <button class="card-button">Add to Cart</button>
    </div>
</div>
```

### 2. Styling Requirements

#### Card Container
- Max width of 350px
- Box shadow for depth effect
- Rounded corners (border-radius)
- Smooth hover effect (scale up slightly)

#### Image
- Full width of card
- Fixed aspect ratio (16:9 or similar)
- object-fit to prevent distortion
- Subtle zoom effect on hover

#### Badge
- Positioned absolutely in top-right corner
- Bright background color
- Small, rounded pill shape

#### Typography
- Use CSS custom properties (variables) for colors
- Different font weights for title vs description
- Line clamping for description (max 2 lines)

#### Price Display
- Flexbox layout
- Original price with strikethrough
- Sale price in a different color

#### Button
- Full width
- Hover and active states
- Transition effects
- Cursor pointer

### 3. Responsive Design
- Cards should display in a grid (3 columns on desktop)
- 2 columns on tablet (max-width: 992px)
- 1 column on mobile (max-width: 576px)

### 4. CSS Features to Use
- CSS Custom Properties (variables)
- Flexbox or Grid
- Transitions and transforms
- Pseudo-classes (:hover, :active, :focus)
- Media queries
- Box model properties

## Bonus Challenges
1. Add a color scheme toggle (light/dark mode using CSS variables)
2. Add a "sold out" state with overlay
3. Implement a skeleton loading animation

## Skills Tested
- CSS Box Model
- Flexbox/Grid layouts
- CSS Variables
- Transitions and transforms
- Responsive design with media queries
- CSS selectors and specificity
